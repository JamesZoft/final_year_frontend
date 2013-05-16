package com.james.erebus.networking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.UnknownHostException;

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
import com.james.erebus.misc.AppConsts;

import android.content.Context;
import android.os.Build;
import android.util.Log;


public abstract class Retriever {

	/**
	 * @param args
	 */

	protected final String baseUrl = "http://teamfrag.net:3001";

	public JSONArray retrieve(URI uri, String filename)
	{

		try{
			File f = new File(AppConsts.currentActivity.getFilesDir() + "/" + filename);
			Log.v("newfilename", f.getCanonicalPath());
			boolean needToDownload = false;
			if(f.length() == 0) //returns 0 if file doesnt exist or length is 0
			{
				needToDownload = true;
				f.createNewFile(); //create a new file, doesn't matter here because it either doesn't exist or has nothing in it
			}
			if(!needToDownload)
			{
				FileInputStream fis = AppConsts.currentActivity.openFileInput(filename);
				JSONArray ja;
				int ch;
				StringBuffer strBuf = new StringBuffer("");
				while((ch = fis.read()) != -1)
				{
					strBuf.append((char)ch);
				}
				fis.close();
				ja = new JSONArray(strBuf.toString());
				return ja;
			}
			else
			{
				return forceRetrieveFromServer(uri, filename);
			}
		}  catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public JSONArray forceRetrieveFromServer(URI uri, String filename) throws UnknownHostException
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
					JSONArray retrievedInfo = new JSONArray(jt);
					FileOutputStream fos = AppConsts.currentActivity.openFileOutput(filename, Context.MODE_PRIVATE);
					fos.write(retrievedInfo.toString().getBytes());
					fos.close();
					return retrievedInfo;
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