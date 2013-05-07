package com.james.erebus.networking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class AddDeviceTask extends TimerTask{
	
	private static String regId;
	private static boolean success;

	@SuppressWarnings({ "deprecation", "serial" })
	@Override
	public void run() {
		try{
			MiscNetworkingHelpers.postInformationToServer(regId, "devices.json", 
					new ArrayList<BasicNameValuePair>() { { add(new BasicNameValuePair("gcm_device[registration_id]", regId)); } });
			success = true;
		} catch (HttpHostConnectException e) {
			success = false;
			Log.e("registerDeviceWithServer", "Failed to register, re-scheduling...");
			AddDeviceTask task = new AddDeviceTask();
			Timer t = new Timer("AddDeviceTimer");
			Date d = new Date();
			d.setDate(Calendar.getInstance().getTime().getDate());
			d.setHours(Calendar.getInstance().getTime().getHours());
			d.setMinutes(Calendar.getInstance().getTime().getMinutes());
			d.setSeconds(Calendar.getInstance().getTime().getSeconds() + 10);
			t.schedule(task, d);
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

}
