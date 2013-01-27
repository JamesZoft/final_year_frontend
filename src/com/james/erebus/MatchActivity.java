package com.james.erebus;
 
import java.util.ArrayList;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
	  TableLayout matches = (TableLayout)findViewById(R.id.matchTable);
	  if(selectedItems == null)
	  {
		  throw new IllegalStateException("selectedItems null");
	  }
	  if(selectedItems.contains(TournyMatchOptions.subbed))
	  {
		  Log.d("filterResults2", "removed subbed things");
	  }
	  if(selectedItems.contains(TournyMatchOptions.unsubbed))
	  {
		  Log.d("filterResults2", "removed unsubbed things");
	  }
	  
	  for(Object o : selectedItems)
	  {
		  System.out.println(o);
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
	setContentView(R.layout.activity_match);
	//confirmPrefs();
  }
}