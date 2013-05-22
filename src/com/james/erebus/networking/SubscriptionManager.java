package com.james.erebus.networking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.content.Context;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;

/**
 * A class to manage writing and reading subscriptions
 * @author james
 *
 */

public class SubscriptionManager {

	/**
	 * Writes a {@link com.james.erebus.JSONJava.JSONArray} of information to the specified file
	 * @param c The {@link android.content.Context} in which this method is called
	 * @param ja The JSONArray of information to be written
	 * @param fileName The name of the file to write to
	 */
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

	/**
	 * Reads a {@link com.james.erebus.JSONJava.JSONArray} of information from the specified file
	 * @param c The {@link android.content.Context} in which this method is called
	 * @param fileName The name of the file to write to
	 * @return A JSONArray of the information read from the file
	 */
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
		} catch(FileNotFoundException fnf)
		{

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