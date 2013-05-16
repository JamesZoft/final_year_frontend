package com.james.erebus.core;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
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
import com.james.erebus.misc.AppConsts;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.GetMatchesTask;
import com.james.erebus.networking.MatchRetriever;
import com.james.erebus.networking.MatchSubscriptionManager;
import com.james.erebus.networking.NotificationManager;

public class MatchActivity extends Activity implements MatchPreferencesFragment.NoticeDialogListener, OnClickListener {

	private static ArrayList<TournyMatchOptions> selectedItems;
	private JSONArray matches;
	private LinearLayout layout;


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

	public void freeTextSearch(View v) throws URISyntaxException
	{
		EditText et = (EditText) findViewById(com.james.erebus.R.id.searchTextMatches);
		ArrayList<String> searchWords = new ArrayList<String>(Arrays.asList(et.getText().toString().split(" ")));
		ArrayList<Button> buttons = new ArrayList<Button>();
		//final Context matchButtonContext = this;
		for(int i = 0; i < matches.length(); i++)
		{
			try {
				JSONObject obj = matches.getJSONObject(i);
				final String values = MiscJsonHelpers.getValuesFromJsonObject(obj);
				for(String s : searchWords)
				{
					if(values.contains(s))
					{
					//	final URI uri = new URI("");
						Button newButton = new Button(this); //construct a button
						if(obj.getString("parentTournament").length() != 0) //some if/elses for setting the text
							newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + ": " + obj.getString("parentTournament") + " (" + obj.getString("status") + ")");
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

	/*private Match matchIsInList(Match m, List<Match> matches)
	{
		if(!matches.isEmpty())
		{
			for(Match match : matches)
			{
				if(match.equals(m))
					return match;
			}
		}
		return null;
	}
	
	*/
	
	public void refresh(View v)
	{
		getMatches(true);
		try {
			displayMatches();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onResume()
	{
		AppConsts.currentActivity = this;
		super.onResume();
	}


	private void getMatches(boolean forceRefresh)
	{
		MatchRetriever m = new MatchRetriever();
		if(forceRefresh)
			try {
				matches = m.forceRetrieveFromServer(m.getURI(), m.getMatchesFilename());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			matches = m.retrieve(m.getURI(), m.getMatchesFilename());
		MatchSubscriptionManager msm = new MatchSubscriptionManager();
		//NotificationManager nm = new NotificationManager();
		ArrayList<Match> newMatches = msm.compareSubbedMatches(this);
		if(newMatches != null && !newMatches.isEmpty())
		{
			NotificationManager.setChangedMatches(newMatches);
		}
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
			Match match = MiscJsonHelpers.jsonToMatch(obj);
			//final String values = MiscJsonHelpers.getValuesFromJsonObject(obj);
			Button newButton = new Button(this); //construct a button
			if(obj.getString("parentTournament").length() != 0) //some if/elses for setting the text
				newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + ": " + obj.getString("parentTournament") + " (" + obj.getString("status") + ")");
			else
				newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + "(" + obj.getString("status") + ")");

			newButton.setOnClickListener(this);
			newButton.setTag(obj);
			MatchSubscriptionManager msm = new MatchSubscriptionManager();
			boolean toBeAdded = true;
			if(matchOptions == null) //if the user hasnt clicked the filter button yet
			{
				matchButtons.add(newButton);
				continue;
			}
			if(matchOptions.isEmpty()) // if the user deselected all of the filters
			{
				matchButtons.add(newButton);
				continue;
			}
			if(!match.getStatus().equals("future") && matchOptions.contains(TournyMatchOptions.future))
			{
				toBeAdded = false;
			}
			//if the user only wants matches in the past
			if(!match.getStatus().equals("past") && matchOptions.contains(TournyMatchOptions.past))
			{
				toBeAdded = false;
			}
			//if the user only wants matches that are ongoing 
			if(!match.getStatus().equals("ongoing") && matchOptions.contains(TournyMatchOptions.ongoing))
			{
				toBeAdded = false;
			}
			if(!msm.isMatchSubbed(this, match) && matchOptions.contains(TournyMatchOptions.subbed)) // if it's in the list, it's subbed to
			{
				toBeAdded = false;
			}
			if(msm.isMatchSubbed(this, match) && matchOptions.contains(TournyMatchOptions.unsubbed))  // if it's not in the list, it's not subbed to
			{
				toBeAdded = false;
			}
			if(toBeAdded)
				matchButtons.add(newButton);
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
		this.setTitle("Matches");
		getMatches(false);
		try {
			displayMatches();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		long thirtyMinutesInMillis = 18000000;
		long fiveminutesinmillis = 300000;
		long delayAndTimer = thirtyMinutesInMillis;
		GetMatchesTask task = new GetMatchesTask();
		GetMatchesTask.setContext(this);
		Timer timer = new Timer("GetMatchesTimer");
		timer.schedule(task, delayAndTimer, delayAndTimer);
	}

	@Override
	public void onClick(View v) {
		JSONObject values = (JSONObject)v.getTag();
		Intent intent = new Intent(this, MatchButtonActivity.class);
		intent.putExtra("com.james.erebus.MatchButtonActivity.dataValues", values);
		startActivity(intent);
	}
}