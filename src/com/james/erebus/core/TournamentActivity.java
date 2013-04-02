package com.james.erebus.core;

import java.util.ArrayList;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.networking.TournamentRetriever;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class TournamentActivity extends Activity implements TournamentPreferencesFragment.NoticeDialogListener{

	private static ArrayList<TournyMatchOptions> selectedItems;

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
	/*
	  private void filterResults()
	  {
		  @SuppressWarnings("unused")
		  TableLayout matches = (TableLayout)findViewById(R.id.tournyTable);
		  if(selectedItems == null)
		  {
			  throw new IllegalStateException("selectedItems null");
		  }
		  if(selectedItems.contains(TournyMatchOptions.subbed))
		  {
			  Log.d("filterResults2", "removed subbed things from tournament things");
		  }
		  if(selectedItems.contains(TournyMatchOptions.unsubbed))
		  {
			  Log.d("filterResults2", "removed subbed things from tournament things");
		  }

		  for(TournyMatchOptions o : selectedItems)
		  {
			  System.out.println("tournyitem: " + o);
		  }
	  }*/

	public static ArrayList<TournyMatchOptions> getSelectedItems()
	{
		return selectedItems;
	}

	private void setSelectedItems(ArrayList<TournyMatchOptions> items)
	{
		selectedItems = items;
	}

	private void displayTournaments() throws JSONException
	{
		TournamentRetriever t = new TournamentRetriever();
		JSONArray tournaments = t.retrieve(t.getURI()); //get all matches from match database

		if(tournaments == null)
		{
			//do something here, means user had no internet to get the matches
		}
		LinearLayout layout = (LinearLayout) findViewById(com.james.erebus.R.id.tournamentButtonsLayout);
		ArrayList<TournyMatchOptions> tournamentOptions = getSelectedItems(); //get the filters that were selected
		layout.removeAllViews();
		ArrayList<Button> tournamentButtons = new ArrayList<Button>();
		for(int i = 0; i < tournaments.length(); i++) //for each match
		{
			JSONObject obj = (JSONObject) tournaments.get(i); //construct a json object for it
			Button newButton = new Button(this); //construct a button

			newButton.setText(obj.getString("name") + ", status: " + obj.getString("status"));

			newButton.setOnClickListener(new View.OnClickListener() { // set the onClick method

				@Override
				public void onClick(View v) {
					//
				}
			});
			if(tournamentOptions == null) //if the user hasnt clicked the filter button yet
			{
				tournamentButtons.add(newButton);
			}
			else if(tournamentOptions.isEmpty()) // if the user deselected all of the filters
			{
				tournamentButtons.add(newButton);
			}
			else if(obj.getBoolean("future") == true && tournamentOptions.contains(TournyMatchOptions.future))
			{
				tournamentButtons.add(newButton);
			}
			//if the user only wants matches in the past
			else if(obj.getBoolean("past") == true && tournamentOptions.contains(TournyMatchOptions.past))
			{
				tournamentButtons.add(newButton);
			}
			//if the user only wants matches that are ongoing 
			else if(obj.getBoolean("ongoing") == true && tournamentOptions.contains(TournyMatchOptions.ongoing))
			{
				tournamentButtons.add(newButton);
			}
		}

		for(Button newButton : tournamentButtons)
		{
			newButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			layout.addView(newButton);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_tournament);
		try {
			displayTournaments();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
