package com.james.erebus.networking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

public class AddDeviceTask extends TimerTask{
	
	private static String regId;
	private static boolean success;
	private static int failures;
	private static ArrayList<AlertDialog> dialogs = new ArrayList<AlertDialog>();
	private static Context context;

	@SuppressWarnings({ "deprecation", "serial" })
	@Override
	public void run() {
		try{
			MiscNetworkingHelpers.postInformationToServer(AddDeviceTask.regId, "devices.json", 
					new ArrayList<BasicNameValuePair>() { { add(new BasicNameValuePair("gcm_device[registration_id]", AddDeviceTask.regId)); } });
			success = true;
			for(AlertDialog retryDialog : AddDeviceTask.dialogs)
			{
				retryDialog.dismiss();
			}
		} catch (HttpHostConnectException e) {
			success = false;
			if(AddDeviceTask.failures < 2)
			{
				Log.e("AddDeviceTask", "Failed to register device, re-trying...");
				AddDeviceTask task = new AddDeviceTask();
				Timer t = new Timer("AddDeviceTimer");
				Date d = Calendar.getInstance().getTime();
				d.setSeconds(Calendar.getInstance().getTime().getSeconds() + 10);
				t.schedule(task, d);
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(AddDeviceTask.context);
						builder.setMessage("No connection to the server - retrying")
						.setTitle("Connection error");
						AlertDialog dialog = builder.create();
						AddDeviceTask.dialogs.add(dialog);
						dialog.show();
					}  });
				AddDeviceTask.failures++;
			}
			else
			{
				MiscNetworkingHelpers.handler.post(new Runnable() {

					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(AddDeviceTask.context);
						builder.setMessage("No connection to the server - please check your wireless is on and connected to a network")
						.setTitle("Connection error");
						AlertDialog dialog = builder.create();
						for(AlertDialog retryDialog : AddDeviceTask.dialogs)
						{
							retryDialog.dismiss();
						}
						dialog.show();
					}  });
				AddDeviceTask.failures = 0;
			}
		} catch (Exception e) {
			Log.e("run", "otherexcpetion");
			e.printStackTrace();
		}
		
		
	}
	
	public boolean getSuccess()
	{
		return success;
	}
	
	public void setRegId(String regId)
	{
		AddDeviceTask.regId = regId;
	}
	
	public static void setContext(Context c)
	{
		AddDeviceTask.context = c;
	}

}
