package com.james.erebus.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;

import com.james.erebus.core.Tournament;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

/**
 * A {@link java.util.TimerTask} for adding a {@link com.james.erebus.core.Tournament} subscription to the server
 * @author james
 *
 */

public class AddTournamentSubscriptionToServerTask extends TimerTask {

	private static String regId;
	private static String tournamentEntryId;
	private static boolean success;
	private static Button b;
	private static int failures;
	private static ArrayList<AlertDialog> dialogs = new ArrayList<AlertDialog>();
	private static Context context;
	private static Tournament tournament;

	@SuppressWarnings({ "serial", "deprecation" })
	@Override
	public void run() {
		try {
			AddTournamentSubscriptionToServerTask.success = MiscNetworkingHelpers.postInformationToServer(AddTournamentSubscriptionToServerTask.regId,
					"subscriptions.json", 
					new ArrayList<BasicNameValuePair>() {{ 
						add(new BasicNameValuePair("subscription[device_registration_id]",
								AddTournamentSubscriptionToServerTask.regId));
						add(new BasicNameValuePair("subscription[model_id]", 
								AddTournamentSubscriptionToServerTask.tournamentEntryId));
						add(new BasicNameValuePair("subscription[model_type]", "TournamentEntry"));
					}});
			if(AddTournamentSubscriptionToServerTask.success)
			{
				TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
				tsm.subToTournament(AddTournamentSubscriptionToServerTask.tournament, 
						AddTournamentSubscriptionToServerTask.context, AddTournamentSubscriptionToServerTask.b);
				for(AlertDialog retryDialog : AddTournamentSubscriptionToServerTask.dialogs)
				{
					retryDialog.dismiss();
				}
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						b.setClickable(true);
						b.setEnabled(true);
						AddTournamentSubscriptionToServerTask.b.setText("Subscribed");
					}  });
			}
			else
			{
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						b.setClickable(true);
						b.setEnabled(true);
						AddTournamentSubscriptionToServerTask.b.setText("Subscribed");
					}  });
			}
			
		} 
		catch(HttpHostConnectException e)
		{
			if(failures < 2)
			{
				Log.e("AddTournamentSubscriptionToServerTask", "Failed to add tournament, re-adding...");
				AddTournamentSubscriptionToServerTask task = new AddTournamentSubscriptionToServerTask();
				Timer t = new Timer("AddTournamentSubscriptionToServerTimer");
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
		} catch(IOException e)
		{
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					b.setClickable(true);
					b.setEnabled(true);
				}  });
			Log.e("AddTournamentSubscriptionToServerTask", "io exception happened :(");
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					b.setClickable(true);
					b.setEnabled(true);
				}  });
			Log.e("AddTournamentSubscriptionToServerTask", "other exception happened :(");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the {@link android.content.Context}
	 * @param c The context to set the context field to
	 */
	public void setContext(Context c)
	{
		AddTournamentSubscriptionToServerTask.context = c;
	}
	
	/**
	 * 
	 * @param t The {@link com.james.erebus.core.Tournament} reference to be assigned to the
	 *  Tournament field
	 */
	public void setTournament(Tournament t)
	{
		AddTournamentSubscriptionToServerTask.tournament = t;
	}

	/**
	 * Sets the {@link android.widget.Button} reference to be used
	 * @param b The Button reference to set the Button field to
	 */
	public void setButton(Button b)
	{
		AddTournamentSubscriptionToServerTask.b = b;		
	}


	/**
	 * Sets the registration id
	 * @param regId The String to set the registration id to
	 */
	public void setRegId(String regId)
	{
		AddTournamentSubscriptionToServerTask.regId = regId;
	}

	/**
	 * Sets the tournament id that is being subscribed to
	 * @param tournamentEntryId The String to set the tournament id to
	 */
	public void setTournamentEntryId(String tournamentEntryId)
	{
		AddTournamentSubscriptionToServerTask.tournamentEntryId = tournamentEntryId;
	}

}
