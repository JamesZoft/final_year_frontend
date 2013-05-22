package com.james.erebus.networking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.HttpHostConnectException;

import android.app.AlertDialog;
import android.util.Log;
import android.widget.Button;

import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;

/**
 * A {@link java.util.TimerTask} for removing a tournament subscription from the server
 * @author james
 *
 */

public class RemoveTournamentSubscriptionFromServerTask extends TimerTask{
	
	private static JSONObject obj;
	private static String regId;
	private static boolean success;
	private static Button b;
	private static int failures;
	private static ArrayList<AlertDialog> dialogs = new ArrayList<AlertDialog>();

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			success = MiscNetworkingHelpers.deleteInformationFromServer(regId, "subscriptions/" + Integer.toString(obj.getInt("id")) + ".json");
			if(success)
			{
				for(AlertDialog retryDialog : dialogs)
				{
					retryDialog.dismiss();
				}
				MiscNetworkingHelpers.handler.post(new Runnable() {
					@Override
					public void run() {
						b.setClickable(true);
						b.setEnabled(true);
						b.setText("Unsubscribed");
					}  });
			}
			else
			{
				MiscNetworkingHelpers.handler.post(new Runnable() {
					@Override
					public void run() {
						b.setClickable(true);
						b.setEnabled(true);
					}  });
			}
		} 
		catch (JSONException e) {
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					b.setClickable(true);
					b.setEnabled(true);
				}  });
			e.printStackTrace();
		} 
		catch(HttpHostConnectException e)
		{
			if(failures < 2)
			{
				Log.e("RemoveTournamentSubscriptionFromServerTask", "Failed to delete tournament, re-trying...");
				RemoveTournamentSubscriptionFromServerTask task = new RemoveTournamentSubscriptionFromServerTask();
				Timer t = new Timer("RemoveTournamentSubscriptionFromServerTimer");
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
						b.setClickable(true);
						b.setEnabled(true);
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
		}
		catch (Exception e) {
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					b.setClickable(true);
					b.setEnabled(true);
				}  });
			e.printStackTrace();
		}
	}

	/**
	 * Sets the {@link android.widget.Button} reference to be used
	 * @param b The Button reference to set the Button field to
	 */
	public void setButton(Button b)
	{
		RemoveTournamentSubscriptionFromServerTask.b = b;		
	}

	/**
	 * Sets the registration id
	 * @param regId The String to set the registration id to
	 */
	public void setRegId(String regId)
	{
		RemoveTournamentSubscriptionFromServerTask.regId = regId;
	}

	/**
	 * Sets the {@link com.james.erebus.JSONJava.JSONObject} to be used in this task
	 * @param obj the JSONObject to be used
	 */
	public void setJsonTournamentObject(JSONObject obj)
	{
		RemoveTournamentSubscriptionFromServerTask.obj = obj;
	}

}
