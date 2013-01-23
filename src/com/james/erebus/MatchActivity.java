package com.james.erebus;
 
import java.util.ArrayList;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
 
public class MatchActivity extends Activity {
 
  private static ArrayList selectedItems;
 
  public void confirmPrefs(View v) {
	    DialogFragment newFragment = new PreferencesFragment();
	    newFragment.show(getFragmentManager(), "missiles");
	}
  
  public static void okButtonPressed(ArrayList items)
  {
	  setSelectedItems(items);
	  //filterResults();
  }
  
  private static void filterResults()
  {
	  
  }
  
  private static void setSelectedItems(ArrayList items)
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