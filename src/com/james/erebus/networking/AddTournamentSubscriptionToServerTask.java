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


public class AddTournamentSubscriptionToServerTask extends TimerTask {

	private static String regId;
	private static String tournamentEntryId;
	private static boolean success;
	private static Button b;
	
	@SuppressWarnings("serial")
	@Override
	public void run() {
		try {
			success = MiscNetworkingHelpers.postInformationToServer(regId, "subscriptions.json", 
					new ArrayList<BasicNameValuePair>() {{ 
						add(new BasicNameValuePair("subscription[device_registration_id]", regId));
						add(new BasicNameValuePair("subscription[model_id]", tournamentEntryId));
						add(new BasicNameValuePair("subscription[model_type]", "TournamentEntry"));
					}});
			if(success)
			{
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						b.setText("Subscribed");
					}  });
			}
			else
			{
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(b.getContext());
						builder.setMessage("No connection to the server - please check your wireless is on and connected to a network")
						.setTitle("Connection error");
						AlertDialog dialog = builder.create();
						dialog.show();
					}  });


				//atempting to initialize hardware accel
			}
		} catch(HttpHostConnectException e)
		{
			Log.e("AddTournamentSubscriptionToServerTask", "Failed to add match, re-adding...");
			AddTournamentSubscriptionToServerTask task = new AddTournamentSubscriptionToServerTask();
			Timer t = new Timer("AddTournamentSubscriptionToServerTimer");
			Date d = new Date();
			d.setDate(Calendar.getInstance().getTime().getDate());
			d.setHours(Calendar.getInstance().getTime().getHours());
			d.setMinutes(Calendar.getInstance().getTime().getMinutes());
			d.setSeconds(Calendar.getInstance().getTime().getSeconds() + 10);
			t.schedule(task, d);
		} catch(IOException e)
		{
			Log.e("AddMatchSubscriptionTask", "io exception happened :(");
		}
		catch (Exception e) {
			Log.e("AddMatchSubscriptionTask", "other exception happened :(");
			e.printStackTrace();
		}
	}

	public void setButton(Button b)
	{
		AddTournamentSubscriptionToServerTask.b = b;		
	}

	public boolean getSuccess()
	{
		return success;
	}

	public void setRegId(String regId)
	{
		AddTournamentSubscriptionToServerTask.regId = regId;
	}

	public void setTournamentEntryId(String tournamentEntryId)
	{
		AddTournamentSubscriptionToServerTask.tournamentEntryId = tournamentEntryId;
	}

}
