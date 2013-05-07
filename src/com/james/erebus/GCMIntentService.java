package com.james.erebus;


import java.util.Calendar;
import java.util.Timer;


import com.google.android.gcm.GCMBaseIntentService;
import com.james.erebus.networking.AddDeviceTask;
import com.james.erebus.networking.MiscNetworkingHelpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class GCMIntentService extends GCMBaseIntentService{

	private String uri = "http://teamfrag.net:3002/";
	
	public GCMIntentService()
	{
		super("585651294813");
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.v("onError", "error!");

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.v("onMessage", "content: " + intent.getExtras().toString());

	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.v("onUnregistered", "unregistered now!");

	}

	@Override
	protected void onRegistered(Context context, String regId) {
		//send reg id to server
		Log.v("onRegistered", "send regId to server");
		addDeviceToServer(regId);
		MiscNetworkingHelpers.regId = regId;
	}


	private void addDeviceToServer(final String regId)
	{
		AddDeviceTask task = new AddDeviceTask();
		task.setRegId(regId);
		Timer t = new Timer("AddDeviceTimer");
		t.schedule(task, Calendar.getInstance().getTime());
	}
}
