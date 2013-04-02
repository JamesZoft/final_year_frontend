package com.james.erebus.networking;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONTokener;

import android.os.Build;


public abstract class Retriever {

	/**
	 * @param args
	 */

	protected final String baseUrl = "http://teamfrag.net:3001";

	public JSONArray retrieve(URI uri)
	{
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams httpParameters = httpclient.getParams();
			HttpConnectionParams.setTcpNoDelay(httpParameters, true); 
			HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
			HttpGet httpget = new HttpGet(uri);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					JSONTokener jt = new JSONTokener(instream);
					JSONArray matches = new JSONArray(jt);
					return matches;
				} finally {
					instream.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public abstract void updatePage(); 

	public abstract void getByPast();

	public abstract void getByFuture();

	public abstract void getByOngoing();

	protected void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}
}
