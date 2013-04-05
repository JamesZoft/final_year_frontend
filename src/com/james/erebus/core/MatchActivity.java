package com.james.erebus.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;
import java.util.HashMap;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.MatchRetriever;

public class MatchActivity extends Activity implements MatchPreferencesFragment.NoticeDialogListener, OnClickListener {

	private static ArrayList<TournyMatchOptions> selectedItems;
	JSONArray matches;
	LinearLayout layout;


	public void confirmPrefs(View v) {
		DialogFragment newFragment = new MatchPreferencesFragment();
		newFragment.show(getFragmentManager(), "missiles");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// User touched the dialog's positive button
		setSelectedItems(MatchPreferencesFragment.getSelectedItems());
		try {
			displayMatches();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// User touched the dialog's negative button
		//Do nothing
	}


	public static ArrayList<TournyMatchOptions> getSelectedItems()
	{
		return selectedItems;
	}

	private void setSelectedItems( ArrayList<TournyMatchOptions> items)
	{
		selectedItems = items;
	}

	public void configureDisplayedMatches(LinearLayout layout)
	{

	}

	public boolean refresh(View view)
	{
		boolean refreshComplete = false;

		return refreshComplete;
	}

	public void freeTextSearch(View v) throws URISyntaxException
	{
		EditText et = (EditText) findViewById(com.james.erebus.R.id.searchTextMatches);
		ArrayList<String> searchWords = new ArrayList<String>(Arrays.asList(et.getText().toString().split(" ")));
		ArrayList<Button> buttons = new ArrayList<Button>();
		final Context matchButtonContext = this;
		for(int i = 0; i < matches.length(); i++)
		{
			try {
				JSONObject obj = matches.getJSONObject(i);
				final String values = MiscJsonHelpers.getValuesFromJsonObject(obj);
				for(String s : searchWords)
				{
					if(values.contains(s))
					{
						final URI uri = new URI("");
						Button newButton = new Button(this); //construct a button
						if(obj.getString("parentTournament").length() != 0) //some if/elses for setting the text
							newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + ": " + obj.getString("parentTournament") + "(" + obj.getString("status") + ")");
						else
							newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + "(" + obj.getString("status") + ")");
						newButton.setOnClickListener(this);
						newButton.setTag(obj);
						buttons.add(newButton);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		layout.removeAllViews();
		for(Button newButton : buttons)
		{
			newButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			layout.addView(newButton);
		}


	}

	private void freeTextSearchFilter()
	{
		layout  = (LinearLayout) findViewById(com.james.erebus.R.id.matchButtonsLayout);
		if(matches == null)
		{
			TextView tv = new TextView(this);
			tv.setText("The app was unable to retrieve information from the server: please check you have a" +
					" valid internet connection, then try again.");
			layout.addView(tv);
			return;
		}

	}

	private void getMatches()
	{
		MatchRetriever m = new MatchRetriever();
		matches = m.retrieve(m.getURI());
	}

	private void displayMatches() throws JSONException
	{
		layout  = (LinearLayout) findViewById(com.james.erebus.R.id.matchButtonsLayout);
		if(matches == null)
		{
			TextView tv = new TextView(this);
			tv.setText("The app was unable to retrieve information from the server: please check you have a" +
					" valid internet connection, then try again.");
			layout.addView(tv);
			return;
		}

		ArrayList<TournyMatchOptions> matchOptions = getSelectedItems(); //get the filters that were selected
		layout.removeAllViews();
		ArrayList<Button> matchButtons = new ArrayList<Button>();
		for(int i = 0; i < matches.length(); i++) //for each match
		{
			JSONObject obj = (JSONObject) matches.get(i); //construct a json object for it
			final String values = MiscJsonHelpers.getValuesFromJsonObject(obj);
			Button newButton = new Button(this); //construct a button
			if(obj.getString("parentTournament").length() != 0) //some if/elses for setting the text
				newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + ": " + obj.getString("parentTournament") + "(" + obj.getString("status") + ")");
			else
				newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + "(" + obj.getString("status") + ")");

			newButton.setOnClickListener(this);
			newButton.setTag(obj);
			if(matchOptions == null) //if the user hasnt clicked the filter button yet
			{
				matchButtons.add(newButton);
			}
			else if(matchOptions.isEmpty()) // if the user deselected all of the filters
			{
				matchButtons.add(newButton);
			}
			else if(obj.getString("status").equals("future") && matchOptions.contains(TournyMatchOptions.future))
			{
				matchButtons.add(newButton);
			}
			//if the user only wants matches in the past
			else if(obj.getString("status").equals("past") && matchOptions.contains(TournyMatchOptions.past))
			{
				matchButtons.add(newButton);
			}
			//if the user only wants matches that are ongoing 
			else if(obj.getString("status").equals("ongoing") && matchOptions.contains(TournyMatchOptions.ongoing))
			{
				matchButtons.add(newButton);
			}
		}

		for(Button newButton : matchButtons)
		{
			newButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			layout.addView(newButton);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_match);
		getMatches();
		try {
			displayMatches();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		JSONObject values = (JSONObject)v.getTag();
		Intent intent = new Intent(this, MatchButtonActivity.class);
		intent.putExtra("com.james.erebus.MatchButtonActivity.dataValues", values.toString());
		startActivity(intent);
	}
}