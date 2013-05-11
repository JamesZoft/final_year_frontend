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

public class RemoveTournamentSubscriptionFromServerTask extends TimerTask{
	
	private static JSONObject obj;
	private static String regId;
	private static boolean success;
	private static Button b;
	private static int failures;
	private static ArrayList<AlertDialog> dialogs = new ArrayList<AlertDialog>();

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
						b.setText("Unsubscribed");
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(HttpHostConnectException e)
		{
			if(failures < 2)
			{
				Log.e("RemoveMatchSubscriptionFromServerTask", "Failed to delete match, re-trying...");
				RemoveTournamentSubscriptionFromServerTask task = new RemoveTournamentSubscriptionFromServerTask();
				Timer t = new Timer("RemoveTournamentSubscriptionFromServerTimer");
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
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setButton(Button b)
	{
		RemoveTournamentSubscriptionFromServerTask.b = b;
	}

	public void setRegId(String regId)
	{
		RemoveTournamentSubscriptionFromServerTask.regId = regId;
	}

	public void setJsonMatchObject(JSONObject obj)
	{
		RemoveTournamentSubscriptionFromServerTask.obj = obj;
	}

}
