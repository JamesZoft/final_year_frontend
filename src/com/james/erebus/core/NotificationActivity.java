package com.james.erebus.core;

/**
 * The java file for the {@link com.james.erebus.core.Notification} activity screen - this screen shows all the current notifications
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;

import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.AppConsts;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.NotificationManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class NotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_notification);
		this.setTitle("Notifications");
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		displayNotifications(new View(this));
	}
	
	@Override
	public void onResume()
	{
		AppConsts.currentActivity = this;
		super.onResume();
	}


	/**
	 * Displays all of the {@link com.james.erebus.core.Notification notifications} on screen
	 * @param v The current {@link android.view.View}
	 */
	public void displayNotifications(View v)
	{
 		EditText et = (EditText) findViewById(com.james.erebus.R.id.searchTextNotifications);
		ArrayList<String> searchWords = new ArrayList<String>(Arrays.asList(et.getText().toString().split(" ")));
		
		final LinearLayout notifsL = (LinearLayout) findViewById(com.james.erebus.R.id.notificationsLayout);
		ArrayList<Notification> notifications = NotificationManager.getNotifcations();
		notifsL.removeAllViews();
		for(final Notification n : notifications)
		{
			boolean notifShouldBeDisplayed = false;
			if(!searchWords.isEmpty())
			{
				for(String s : searchWords)
				{
					if(n.getText().contains(s))
					{
						notifShouldBeDisplayed = true;
					}
				}
			}
			if(!notifShouldBeDisplayed)
				continue;
			final Context c = notifsL.getContext();
			final LinearLayout notifL = new LinearLayout(c);
			final TextView tv = new TextView(c);
			tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
			tv.setText(n.getText());
			final Button viewButton = new Button(c);
			viewButton.setText("View");
			viewButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					if(n.tournyOrMatch() == 0)
					{
						JSONObject values = MiscJsonHelpers.matchToJson(n.getMatch());
						Intent intent = new Intent(c, MatchButtonActivity.class);
						intent.putExtra("com.james.erebus.MatchButtonActivity.dataValues", values);
						startActivity(intent);
					}
					else if(n.tournyOrMatch() == 1)
					{
						JSONObject values = MiscJsonHelpers.tournamentToJson(n.getTournament());
						Intent intent = new Intent(c, TournamentButtonActivity.class);
						intent.putExtra("com.james.erebus.TournamentButtonActivity.dataValues", values);
						startActivity(intent);
					}
				}
				
			});
			final Button clearButton = new Button(c);
			clearButton.setText("Clear");
			clearButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					NotificationManager.removeNotification(n);
					notifsL.removeView(notifL);
				}
			});
			notifL.addView(tv);
			notifL.addView(clearButton);
			notifL.addView(viewButton);
			notifsL.addView(notifL, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}