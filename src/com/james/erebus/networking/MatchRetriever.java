package com.james.erebus.networking;

import java.io.BufferedInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.JSONJava.JSONTokener;

public class MatchRetriever extends Retriever{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MatchRetriever m = new MatchRetriever();
		m.getMatches();

	}
	
	public JSONArray getMatches()
	{
		//disableConnectionReuseIfNecessary();
		
		try {
			URI uri = new URI("http://teamfrag.net:3001/matches.json");
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
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
       
	}

	public void getByPlayer()
	{
		
	}

	@Override
	public void getByOngoing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByPast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByFuture() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePage() {
		// TODO Auto-generated method stub
		
	}

}
