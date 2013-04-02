package com.james.erebus.core;

import java.util.ArrayList;

import com.james.erebus.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.view.View;

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
		  //filterResults();
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
	  
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournament);
		//confirmPrefs();
	  }

}
