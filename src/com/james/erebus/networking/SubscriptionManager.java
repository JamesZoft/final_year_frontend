package com.james.erebus.networking;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.JSONJava.JSONTokener;

public class SubscriptionManager {

		public void writeSubbed(JSONArray ja, String fileName)
		{
			File f = new File(fileName);
			if(!f.exists())
			{
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				f.canRead();
				f.canWrite();
			}
			try{
				FileWriter bw = new FileWriter(f);
				for(int i = 0; i < ja.length(); i++)
				{
					JSONObject obj = ja.getJSONObject(i);
					obj.write(bw);
				}
				bw.flush();
				bw.close();
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		public JSONArray readSubbed(String fileName)
		{
			File f = new File(fileName);
			if(!f.exists())
			{
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				f.canRead();
				f.canWrite();
			}
			try{
				FileReader fir = new FileReader(f);
				JSONTokener jtoken = new JSONTokener(fir);
				JSONArray ja1 = new JSONArray();
				while(jtoken.more())
				{
					ja1.put(jtoken.nextValue());
				}
				System.out.println(ja1.toString());
				fir.close();
				return ja1;
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
}
