package com.james.erebus.core;

import java.util.ArrayList;


import com.james.erebus.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.networking.MatchRetriever;
 
public class MatchActivity extends Activity implements MatchPreferencesFragment.NoticeDialogListener {
 
	private static ArrayList<TournyMatchOptions> selectedItems;
	
	
 
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
	 // try { 
		
	//} catch (MalformedURLException e) {
	//	e.printStackTrace();
	//} catch (IOException e) {
	//	e.printStackTrace();
	//}
	  
	  /*
	  AndroidHttpClient c = AndroidHttpClient.newInstance("");
		HttpGet g;
		try {
			g = new HttpGet(new URI("192.168.0.11:3000"));
			HttpResponse r = c.execute(null, g); 
			Log.i("trying http","" + r.getStatusLine());
			r.getEntity().consumeContent();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	  
	  /*
	   
}
	   */
  }

  @Override
  public void onDialogNegativeClick(DialogFragment dialog) {
      // User touched the dialog's negative button
      //Do nothing
  }
  /*
  private void filterResults()
  {
	  //TableLayout matches = (TableLayout)findViewById(R.id.matchButtonsLayout);
	  if(selectedItems == null)
	  {
		  throw new IllegalStateException("selectedItems null");
	  }
	  if(selectedItems.contains(TournyMatchOptions.subbed))
	  {
		  Log.d("filterResults2", "removed subbed things from match things");
	  }
	  if(selectedItems.contains(TournyMatchOptions.unsubbed))
	  {
		  Log.d("filterResults2", "removed unsubbed things from match things");
	  }
	  
	  for(Object o : selectedItems)
	  {
		  System.out.println("matchitem: " +o);
	  }
  }*/
  
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
  
  private void displayMatches() throws JSONException
  {
	  MatchRetriever m = new MatchRetriever();
	  JSONArray matches = m.getMatches(); //get all matches from match database
	  
	  if(matches == null)
	  {
		  //do something here, means user had no internet to get the matches
	  }
	  LinearLayout layout = (LinearLayout) findViewById(R.id.matchButtonsLayout);
	  ArrayList<TournyMatchOptions> matchOptions = getSelectedItems(); //get the filters that were selected
	  layout.removeAllViews();
	  ArrayList<Button> matchButtons = new ArrayList<Button>();
	  for(int i = 0; i < matches.length(); i++) //for each match
	  {
		  JSONObject obj = (JSONObject) matches.get(i); //construct a json object for it
		  Button newButton = new Button(this); //construct a button
		  if(obj.getString("parentTournament").length() != 0) //some if/elses for setting the text
			  newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2") + ": " + obj.getString("parentTournament"));
		  else
			  newButton.setText(obj.getString("player1") + " vs " + obj.getString("player2"));
		  newButton.setOnClickListener(new View.OnClickListener() { // set the onClick method
			
			@Override
			public void onClick(View v) {
				//
			}
		  });
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
		  else if(obj.getString("status").equals("ongoing") && matchOptions.contains(MatchOptions.ongoing))
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
	setContentView(R.layout.activity_match);
	try {
		displayMatches();
	} catch (JSONException e) {
		e.printStackTrace();
	}
  }
}