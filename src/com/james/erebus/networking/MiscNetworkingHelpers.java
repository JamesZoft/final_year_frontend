package com.james.erebus.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class MiscNetworkingHelpers {

	public static String regId;
	
	public static Handler handler = new Handler(Looper.getMainLooper());

	public static boolean postInformationToServer(String regId, String uriExtension, ArrayList<BasicNameValuePair> info) throws Exception
	{
		//try{
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
			Log.v("postInformationToServer", "success!");
			return true;
		}
		else
		{
			Log.v("postInformationToServer", "failure :(");
			return false;
		}

		//	}  
		//catch (IOException e) 
		//{
		//	e.printStackTrace();
		//}
	}

	public static boolean deleteInformationFromServer(String regId, String uriExtension) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParameters = httpclient.getParams();
		HttpConnectionParams.setTcpNoDelay(httpParameters, true); 
		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

		HttpDelete httpdelete = new HttpDelete("http://teamfrag.net:3002/" + uriExtension);

		HttpResponse response = httpclient.execute(httpdelete);
		Log.v("response_code", Integer.toString(response.getStatusLine().getStatusCode()));
		if(response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300)
		{
			Log.v("deleteInformationFromServer", "success!");
			return true;
		}
		else
		{
			Log.v("deleteInformationFromServer", "failure :(");
			return false;
		}
	} 


}
