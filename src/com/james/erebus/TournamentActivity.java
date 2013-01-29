package com.james.erebus;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.support.v4.app.NavUtils;

public class TournamentActivity extends Activity implements TournamentPreferencesFragment.NoticeDialogListener{

	@SuppressWarnings("rawtypes")
	private static ArrayList selectedItems;
	 
	  public void confirmPrefs(View v) {
		    DialogFragment newFragment = new TournamentPreferencesFragment();
		    newFragment.show(getFragmentManager(), "missiles");
		}
	  
	  @Override
	  public void onDialogPositiveClick(DialogFragment dialog) {
	      // User touched the dialog's positive button
		  setSelectedItems(TournamentPreferencesFragment.getSelectedItems());
		  filterResults();
	  }

	  @Override
	  public void onDialogNegativeClick(DialogFragment dialog) {
	      // User touched the dialog's negative button
	      //Do nothing
	  }
	  
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
		  
		  for(Object o : selectedItems)
		  {
			  System.out.println("tournyitem: " + o);
		  }
	  }
	  
	  @SuppressWarnings("rawtypes")
	public static ArrayList getSelectedItems()
	  {
		  return selectedItems;
	  }
	  
	  private void setSelectedItems(@SuppressWarnings("rawtypes") ArrayList items)
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
