package com.james.erebus;
 
import java.util.ArrayList;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
 
public class MatchActivity extends Activity {
 
  private ArrayList selectedItems;
 
  public void confirmPrefs() {
	    DialogFragment newFragment = new PreferencesFragment();
	    newFragment.show(getFragmentManager(), "missiles");
	}
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	confirmPrefs();
  }
}