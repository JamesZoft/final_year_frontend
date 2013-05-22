package com.james.erebus.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;


import android.app.AlertDialog;
import android.util.Log;
import android.widget.Button;

/**
 * A {@link java.util.TimerTask} for adding a {@link com.james.erebus.core.Match} subscription to the server
 * @author james
 *
 */

public class AddMatchSubscriptionTask extends TimerTask{

	private static String regId;
	private static String matchEntryId;
	private static boolean success;
	private static Button b;
	private static int failures;
	
	private static ArrayList<AlertDialog> dialogs = new ArrayList<AlertDialog>();

	@SuppressWarnings({ "serial", "deprecation" })
	@Override
	public void run() {
		try {
			success = MiscNetworkingHelpers.postInformationToServer(regId, "subscriptions.json", 
					new ArrayList<BasicNameValuePair>() {{ 
						add(new BasicNameValuePair("subscription[device_registration_id]", regId));
						add(new BasicNameValuePair("subscription[model_id]", matchEntryId));
						add(new BasicNameValuePair("subscription[model_type]", "MatchEntry"));
					}});
			if(success)
			{
				
				//MatchSubscriptionManager msm = new MatchSubscriptionManager();
				//msm.subToMatch(AddMatchSubscriptionTask.match, AddMatchSubscriptionTask.context, AddMatchSubscriptionTask.b);
				for(AlertDialog retryDialog : AddMatchSubscriptionTask.dialogs)
				{
					retryDialog.dismiss();
				}
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						AddMatchSubscriptionTask.b.setText("Subscribed");
						AddMatchSubscriptionTask.b.setEnabled(true);
						AddMatchSubscriptionTask.b.setClickable(true);
					}  });
			}
		} 
		catch(HttpHostConnectException e)
		{
			if(failures < 2)
			{
				Log.e("AddMatchSubscriptionTask", "Failed to add match, re-adding...");
				AddMatchSubscriptionTask task = new AddMatchSubscriptionTask();
				Timer t = new Timer("AddMatchSubscriptionTimer");
				Date d = Calendar.getInstance().getTime();
				d.setSeconds(d.getSeconds() + 10);
				t.schedule(task, d);
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(b.getContext());
						builder.setMessage("No connection to the server - retrying")
						.setTitle("Connection error");
						AlertDialog dialog = builder.create();
						dialogs.add(dialog);
						dialog.show();
					}  });
				failures++;
			}
			else
			{
				
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						b.setEnabled(true);
						b.setClickable(true);
						AlertDialog.Builder builder = new AlertDialog.Builder(b.getContext());
						builder.setMessage("No connection to the server - please check your wireless is on and connected to a network")
						.setTitle("Connection error");
						AlertDialog dialog = builder.create();
						for(AlertDialog retryDialog : dialogs)
						{
							retryDialog.dismiss();
						}
						dialog.show();
					}  });
				failures = 0;
			}

		} catch(IOException e)
		{
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					b.setClickable(true);
					b.setEnabled(true);
				}  });
			Log.e("AddMatchSubscriptionTask", "io exception happened :(");
			e.printStackTrace();
		}
		catch (Exception e) {
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					b.setClickable(true);
					b.setEnabled(true);
				}  });
			Log.e("AddMatchSubscriptionTask", "other exception happened :(");
			e.printStackTrace();
		}
	}

	/**
	 * Sets the {@link android.widget.Button} reference to be used
	 * @param b The Button reference to set the Button field to
	 */
	public void setButton(Button b)
	{
		AddMatchSubscriptionTask.b = b;		
	}

	/**
	 * Sets the registration id
	 * @param regId The String to set the registration id to
	 */
	public void setRegId(String regId)
	{
		AddMatchSubscriptionTask.regId = regId;
	}

	/**
	 * Sets the match id that is being subscribed to
	 * @param matchEntryId The String to set the match id to
	 */
	public void setMatchEntryId(String matchEntryId)
	{
		AddMatchSubscriptionTask.matchEntryId = matchEntryId;
	}

}
