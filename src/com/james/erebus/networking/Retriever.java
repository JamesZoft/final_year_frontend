package com.james.erebus.networking;

import java.io.File;
import java.io.FileInputStream;
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

/**
 * Parent class that implements network retrieval of information from the main back-end server
 * @author james
 *
 */

public abstract class Retriever {

	protected final String baseUrl = "http://teamfrag.net:3001";

	/**
	 * Method to retrieve information, first looking in the cache
	 * @param uri The URI to fall back on if the cache is empty
	 * @param filename The cache filename to look in
	 * @return A {@link com.james.erebus.JSONJava.JSONArray} of the information that is returned
	 */
	public JSONArray retrieve(URI uri, String filename)
	{

		try{
			File f = new File(AppConsts.currentActivity.getFilesDir() + "/" + filename);
			Log.v("newfilename", f.getCanonicalPath());
			boolean needToDownload = false;
			if(f.length() <= 2) //returns 0 if file doesnt exist or length is 0, or if the file contains an empty json array []
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

	/**
	 * Retrieves information straight from the server, bypassing the cache
	 * @param uri The URI to retrieve from
	 * @param filename The filename to write the the retrieved information into
	 * @return A {@link com.james.erebus.JSONJava.JSONArray} of the retrieved information
	 * @throws UnknownHostException
	 */
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
}