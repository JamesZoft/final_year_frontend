package com.james.erebus.core;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.james.erebus.MESSAGE";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //RuntimeInlineAnnotationReader.cachePackageAnnotation( , new XmlSchemaMine(""));
    	 //requestWindowFeature(Window.);
    	super.onCreate(savedInstanceState);
        setContentView(com.james.erebus.R.layout.activity_main);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
        
    }

    /**
     * Called when the user clicks the Send button
     */
    public void onMatchButtonPress(View view) {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }
    
    public void onTournamentButtonPress(View view) {
        Intent intent = new Intent(this, TournamentActivity.class);
        startActivity(intent);
    }
    
    public void onNotificationsButtonPress(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }
}
