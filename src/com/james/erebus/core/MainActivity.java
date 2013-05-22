package com.james.erebus.core;

import com.google.android.gcm.GCMRegistrar;
import com.james.erebus.misc.AppConsts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


/**
 * The java file for the Main activity, which is the first screen that appears when starting the application
 * @author james
 *
 */
public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.james.erebus.MESSAGE";
	
	@Override
	public void onResume()
	{
		AppConsts.currentActivity = this;
		super.onResume();
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_main);
		GCMRegistrar.checkDevice(this);

		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			GCMRegistrar.register(this, "585651294813");
		} else {
			Log.v("Gcm register tag", "Already registered");
		}
		Log.v("reg_id", regId);
	}
	
	/**
	 * Called when the user clicks the Matches button
	 * @param view The current {@link android.view.View}
	 */
	public void onMatchButtonPress(View view) {
		Intent intent = new Intent(this, MatchActivity.class);
		startActivity(intent);
	}

	/**
	 * Called when the user clicks the Tournaments button
	 * @param view The current {@link android.view.View}
	 */
	public void onTournamentButtonPress(View view) {
		Intent intent = new Intent(this, TournamentActivity.class);
		startActivity(intent);
	}

	/**
	 * Called when the user clicks the Notifications button
	 * @param view The current {@link android.view.View}
	 */
	public void onNotificationsButtonPress(View view) {
		Intent intent = new Intent(this, NotificationActivity.class);
		startActivity(intent);
	}
}