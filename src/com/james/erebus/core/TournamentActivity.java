package com.james.erebus.core;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.AppConsts;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.GetMatchesTask;
import com.james.erebus.networking.GetTournamentsTask;
import com.james.erebus.networking.NotificationManager;
import com.james.erebus.networking.TournamentRetriever;
import com.james.erebus.networking.TournamentSubscriptionManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TournamentActivity extends Activity implements TournamentPreferencesFragment.NoticeDialogListener, OnClickListener{

	private static ArrayList<TournyMatchOptions> selectedItems;
	JSONArray tournaments;
	LinearLayout layout;

	public void confirmPrefs(View v) {
		DialogFragment newFragment = new TournamentPreferencesFragment();
		newFragment.show(getFragmentManager(), "missiles");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// User touched the dialog's positive button
		setSelectedItems(TournamentPreferencesFragment.getSelectedItems());
		  try {
			displayTournaments();
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

	private void setSelectedItems(ArrayList<TournyMatchOptions> items)
	{
		selectedItems = items;
	}
	
	public void freeTextSearch(View v)
	{
		EditText et = (EditText) findViewById(com.james.erebus.R.id.searchTextTournaments);
		ArrayList<String> searchWords = new ArrayList<String>(Arrays.asList(et.getText().toString().split(" ")));
		ArrayList<Button> buttons = new ArrayList<Button>();
		for(int i = 0; i < tournaments.length(); i++)
		{
			try {
				JSONObject obj = tournaments.getJSONObject(i);
				String values = MiscJsonHelpers.getValuesFromJsonObject(obj);
				for(String s : searchWords)
				{
					if(values.contains(s))
					{
						Button newButton = new Button(this); //construct a button

						newButton.setText(obj.getString("name"));

						newButton.setOnClickListener(this);
						newButton.setTag(obj);
						buttons.add(newButton);
					}
				}
			} catch (JSONException e) {
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

	private void displayTournaments() throws JSONException
	{
		//TournamentRetriever t = new TournamentRetriever();
		//tournaments = t.retrieve(t.getURI()); //get all tournaments from tournament database
		layout = (LinearLayout) findViewById(com.james.erebus.R.id.tournamentButtonsLayout);
		if(tournaments == null)
		{
			TextView tv = new TextView(this);
			  tv.setText("The app was unable to retrieve information from the server: please check you have a" +
			  		" valid internet connection, then try again.");
			  layout.addView(tv);
			  return;
		}
		ArrayList<TournyMatchOptions> tournamentOptions = getSelectedItems(); //get the filters that were selected
		layout.removeAllViews();
		ArrayList<Button> tournamentButtons = new ArrayList<Button>();
		for(int i = 0; i < tournaments.length(); i++) //for each tournament
		{
			JSONObject obj = (JSONObject) tournaments.get(i); //construct a json object for it
			Button newButton = new Button(this); //construct a button
			Tournament tournament = MiscJsonHelpers.jsonToTournament(obj);
			newButton.setText(obj.getString("name"));

			newButton.setOnClickListener(this);
			newButton.setTag(obj);
			TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
			boolean toBeAdded = true;
			if(tournamentOptions == null) //if the user hasnt clicked the filter button yet
			{
				tournamentButtons.add(newButton);
				continue;
			}
			if(tournamentOptions.isEmpty()) // if the user deselected all of the filters
			{
				tournamentButtons.add(newButton);
				continue;
			}
			if(!tournament.getFuture().equals("true") && tournamentOptions.contains(TournyMatchOptions.future))
			{
				toBeAdded = false;
			}
			//if the user only wants tournaments in the past
			if(!tournament.getPast().equals("true") && tournamentOptions.contains(TournyMatchOptions.past))
			{
				toBeAdded = false;
			}
			//if the user only wants tournaments that are ongoing 
			if(!tournament.getOngoing().equals("true") && tournamentOptions.contains(TournyMatchOptions.ongoing))
			{
				toBeAdded = false;
			}
			if(!tsm.isTournamentSubbed(this, tournament) && tournamentOptions.contains(TournyMatchOptions.subbed)) // if it's in the list, it's subbed to
			{
				toBeAdded = false;
			}
			if(tsm.isTournamentSubbed(this, tournament) && tournamentOptions.contains(TournyMatchOptions.unsubbed))  // if it's not in the list, it's not subbed to
			{
				toBeAdded = false;
			}
			if(toBeAdded)
				tournamentButtons.add(newButton);
		}

		for(Button newButton : tournamentButtons)
		{
			newButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			layout.addView(newButton);
		}
	}
	
	public void refresh(View v)
	{
		getTournaments(true);
		try {
			displayTournaments();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getTournaments(boolean forceRefresh)
	{
		TournamentRetriever t = new TournamentRetriever();
		if(forceRefresh)
			try {
				tournaments = t.forceRetrieveFromServer(t.getURI(), t.getTournamentsFilename());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			tournaments = t.retrieve(t.getURI(), t.getTournamentsFilename());
		TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
		//NotificationManager nm = new NotificationManager();
		ArrayList<Tournament> newTournaments = tsm.compareSubbedTournaments(this);
		if(newTournaments != null && !newTournaments.isEmpty())
		{
			NotificationManager.setChangedTournaments(newTournaments);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_tournament);
		this.setTitle("Tournaments");
		getTournaments(false);
		try {
			displayTournaments();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		long fiveminutesinmillis = 300000;
		long thirtyMinutesInMillis = 18000000;
		long delayAndTimer = thirtyMinutesInMillis;
		GetTournamentsTask task = new GetTournamentsTask();
		GetTournamentsTask.setContext(this);
		Timer timer = new Timer("GetTournamentsTimer");
		timer.schedule(task, delayAndTimer, delayAndTimer);
	}

	@Override
	public void onClick(View v) {
		JSONObject values = (JSONObject)v.getTag();
		Intent intent = new Intent(this, TournamentButtonActivity.class);
		intent.putExtra("com.james.erebus.TournamentButtonActivity.dataValues", values);
		startActivity(intent);
	}
	
	@Override
	public void onResume()
	{
		AppConsts.currentActivity = this;
		super.onResume();
	}



}