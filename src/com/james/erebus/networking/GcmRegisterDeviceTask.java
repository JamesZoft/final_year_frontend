package com.james.erebus.networking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class GcmRegisterDeviceTask extends TimerTask{
	
	private static Context context;
	private static int failures;
	private static ArrayList<AlertDialog> dialogs = new ArrayList<AlertDialog>();

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		GCMRegistrar.checkDevice(context);

		GCMRegistrar.checkManifest(context);
		final String regId = GCMRegistrar.getRegistrationId(context);

		if (regId.equals("")) {
			GCMRegistrar.register(context, "585651294813");
		} else {
			Log.v("Gcm register tag", "Already registered");
		}
		Log.v("reg_id", regId);
		
		if(!GCMRegistrar.isRegistered(context) && GcmRegisterDeviceTask.failures < 2)
		{
			GcmRegisterDeviceTask task = new GcmRegisterDeviceTask();
			Timer t = new Timer();
			Date d = Calendar.getInstance().getTime();
			d.setSeconds(d.getSeconds() + 10);
			t.schedule(task, d);
			GcmRegisterDeviceTask.failures++;
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage("Cannot connect to the server, re-trying")
					.setTitle("Connection error");
					AlertDialog dialog = builder.create();
					GcmRegisterDeviceTask.dialogs.add(dialog);
					dialog.show();
				}  });			
		}
		else if(!GCMRegistrar.isRegistered(context) && GcmRegisterDeviceTask.failures >= 2)
		{
			MiscNetworkingHelpers.handler.post(new Runnable() {

				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage("No connection to the server - please check your wireless is on and connected to a network")
					.setTitle("Connection error");
					AlertDialog dialog = builder.create();
					for(AlertDialog retryDialog : GcmRegisterDeviceTask.dialogs)
					{
						retryDialog.dismiss();
					}
					dialog.show();
				}  });
			GcmRegisterDeviceTask.failures = 0;
		}
		else if(!GCMRegistrar.isRegistered(context) && GcmRegisterDeviceTask.failures < 2)
		{
			for(AlertDialog retryDialog : GcmRegisterDeviceTask.dialogs)
			{
				retryDialog.dismiss();
			}
		}
	}

	
	public static void setContext(Context c)
	{
		GcmRegisterDeviceTask.context = c;
	}

}
