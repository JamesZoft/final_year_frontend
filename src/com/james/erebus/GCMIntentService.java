package com.james.erebus;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.google.android.gcm.GCMBaseIntentService;
import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONTokener;
import com.james.erebus.networking.MiscNetworkingHelpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class GCMIntentService extends GCMBaseIntentService{

	private String uri = "http://teamfrag.net:3002/";

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
		MiscNetworkingHelpers.sendInformationToServer(regId, "devices.json", 
				new ArrayList<BasicNameValuePair>() { { add(new BasicNameValuePair("gcm_device[registration_id]", regId)); } });
	}

}
