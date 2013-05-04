package com.james.erebus.networking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;

public class SubscriptionManager {

		public void writeSubbed(Context c, JSONArray ja, String fileName)
		{
			FileOutputStream fos;
			try {
				fos = c.openFileOutput(fileName, Context.MODE_PRIVATE);
				fos.write(ja.toString().getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	
		
		public JSONArray readSubbed(Context c, String fileName)
		{
			try{
				FileInputStream fis = c.openFileInput(fileName);
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
			catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
}