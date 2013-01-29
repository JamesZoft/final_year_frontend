package com.james.erebus;
 
import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
 
public class MatchActivity extends Activity implements MatchPreferencesFragment.NoticeDialogListener {
 
  @SuppressWarnings("rawtypes")
private static ArrayList selectedItems;
 
  public void confirmPrefs(View v) {
	    DialogFragment newFragment = new MatchPreferencesFragment();
	    newFragment.show(getFragmentManager(), "missiles");
	}
  
  @Override
  public void onDialogPositiveClick(DialogFragment dialog) {
      // User touched the dialog's positive button
	  setSelectedItems(MatchPreferencesFragment.getSelectedItems());
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
	  TableLayout matches = (TableLayout)findViewById(R.id.matchButtonsLayout);
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
  
  public void configureDisplayedMatches(LinearLayout layout)
  {
	  
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_match);
	LinearLayout layout = (LinearLayout) findViewById(R.id.matchButtonsLayout);
	Button aButton = new Button(this);
	aButton.setText("somestuff");
	aButton.setLayoutParams(new LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
	layout.addView(aButton);
  }
}