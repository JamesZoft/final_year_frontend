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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

public class AddMatchSubscriptionTask extends TimerTask{

	private static String regId;
	private static String matchEntryId;
	private static boolean success;
	private static Button b;
	private static int failures;
	private static ArrayList<AlertDialog> dialogs = new ArrayList<AlertDialog>();

	@SuppressWarnings("serial")
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
				for(AlertDialog retryDialog : dialogs)
				{
					retryDialog.dismiss();
				}
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						b.setText("Subscribed");
					}  });
			}
			/*else
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


			}*/
		} catch(HttpHostConnectException e)
		{
			if(failures < 2)
			{
				Log.e("AddMatchSubscriptionTask", "Failed to add match, re-adding...");
				AddMatchSubscriptionTask task = new AddMatchSubscriptionTask();
				Timer t = new Timer("AddMatchSubscriptionTimer");
				Date d = new Date();
				d.setDate(Calendar.getInstance().getTime().getDate());
				d.setHours(Calendar.getInstance().getTime().getHours());
				d.setMinutes(Calendar.getInstance().getTime().getMinutes());
				d.setSeconds(Calendar.getInstance().getTime().getSeconds() + 10);
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
			Log.e("AddMatchSubscriptionTask", "io exception happened :(");
			e.printStackTrace();
		}
		catch (Exception e) {
			Log.e("AddMatchSubscriptionTask", "other exception happened :(");
			e.printStackTrace();
		}
	}

	public void setButton(Button b)
	{
		AddMatchSubscriptionTask.b = b;		
	}

	public boolean getSuccess()
	{
		return success;
	}

	public void setRegId(String regId)
	{
		AddMatchSubscriptionTask.regId = regId;
	}

	public void setMatchEntryId(String matchEntryId)
	{
		AddMatchSubscriptionTask.matchEntryId = matchEntryId;
	}

}
