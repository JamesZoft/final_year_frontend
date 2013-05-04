package com.james.erebus.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

public class MiscNetworkingHelpers {
	
	public static String regId;
	
	public static void sendInformationToServer(String regId, String uriExtension, ArrayList<BasicNameValuePair> info)
	{
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams httpParameters = httpclient.getParams();
			HttpConnectionParams.setTcpNoDelay(httpParameters, true); 
			HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

			HttpPost httppost = new HttpPost("http://teamfrag.net:3002/" + uriExtension);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(info.size());
			for(BasicNameValuePair p : info)
				nameValuePairs.add(p);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			Log.v("response_code", Integer.toString(response.getStatusLine().getStatusCode()));
			if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300)
			{
				Log.v("addDeviceToServer", "success!");
			}
			else
				Log.v("addDeviceToServer", "failure :(");
		}  catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
