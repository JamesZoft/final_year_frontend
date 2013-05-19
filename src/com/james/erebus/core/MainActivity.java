package com.james.erebus.core;



import java.util.Calendar;
import java.util.Timer;

import com.google.android.gcm.GCMRegistrar;
import com.james.erebus.misc.AppConsts;
import com.james.erebus.networking.GcmRegisterDeviceTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.james.erebus.MESSAGE";
/*
	private void register() {
		Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
		intent.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		intent.putExtra("sender", "585651294813");
		startService(intent);
	} */
	
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