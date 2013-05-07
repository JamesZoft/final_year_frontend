package com.james.erebus.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.core.Match;
import com.james.erebus.misc.MiscJsonHelpers;

public class MatchSubscriptionManager extends SubscriptionManager {

	private static final String filename = "subbedMatches.json";
	//ArrayList<Match> subbedMatches;

	public MatchSubscriptionManager()
	{
		//subbedMatches = new ArrayList<Match>();
	}

	public boolean isMatchSubbed(Context c, Match m)
	{
		ArrayList<Match> matches = getSubbedMatches(c);
		for(Match match : matches)
		{
			if(m.equalsMatch(match))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param c - The context in which this method is invoked
	 * @return - The ArrayList of updated matches if it's newer, else null
	 */
	public ArrayList<Match> compareSubbedMatches(Context c)
	{
		ArrayList<Match> subbedMatches = getSubbedMatches(c);
		MatchRetriever mr = new MatchRetriever();
		JSONArray ja = mr.retrieve(mr.getURI());
		if(ja == null)
			return null;
		ArrayList<Match> updatedMatches = (ArrayList<Match>) MiscJsonHelpers.jsonMatchArrayToMatchList(ja);
		ArrayList<Match> changedMatches = new ArrayList<Match>();
		JSONArray newSubbedJa = new JSONArray();
		boolean shouldAdd;
		for(Match m : subbedMatches)
		{
			shouldAdd = true;
			for(Match m2 : updatedMatches)
			{
				if(m.equalsMatch(m2))
				{
					if(m.isDifferentTo(m2))
					{
						Log.i("l54:matchsubmanager", "added changed match");
						changedMatches.add(m2);
						newSubbedJa.put(MiscJsonHelpers.matchToJson(m2));
						shouldAdd = false;
						break;
					}
				}
			}
			if(shouldAdd)
				newSubbedJa.put(MiscJsonHelpers.matchToJson(m));
		}
		writeSubbed(c, newSubbedJa, filename);
		return changedMatches;
	}



	public ArrayList<Match> getSubbedMatches(Context c)
	{
		JSONArray ja = readSubbed(c, filename);
		if(ja != null)
		{
			ArrayList<Match> matches = new ArrayList<Match>();
			for(int i = 0; i < ja.length(); i++)
			{
				try {
					matches.add(MiscJsonHelpers.jsonToMatch((JSONObject) ja.get(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return matches;
		}
		else
		{
			return new ArrayList<Match>();
		}
	}


	public boolean subToMatch(Match m, Context c, Button b) throws IOException, JSONException
	{

		JSONArray ja = readSubbed(c, filename);
		if(ja != null)
		{
			ArrayList<Match> subbedMatches = new ArrayList<Match>();
			for(int i = 0; i < ja.length(); i++)
			{
				try {
					subbedMatches.add(MiscJsonHelpers.jsonToMatch((JSONObject) ja.get(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			for(int i = 0; i < subbedMatches.size(); i++)
			{
				if(subbedMatches.get(i).equalsMatch(m))
				{
					return false;
				}
			}
		}
		else
			ja = new JSONArray();
		ja.put(MiscJsonHelpers.matchToJson(m));
		addMatchSubscriptionToServer(MiscNetworkingHelpers.regId, Integer.toString(m.getId()), b);
		writeSubbed(c, ja, filename);
		return false;
	}

	public boolean unsubFromMatch(Context c, Match m, Button b)
	{
		JSONArray ja = readSubbed(c, filename);

		ArrayList<Match> subbedMatches = new ArrayList<Match>();
		for(int i = 0; i < ja.length(); i++)
		{
			try {
				subbedMatches.add(MiscJsonHelpers.jsonToMatch((JSONObject) ja.get(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		JSONArray returnJa = new JSONArray();
		for(int i = 0; i < subbedMatches.size(); i++)
		{
			if(subbedMatches.get(i).equalsMatch(m))
			{
				//retVal = true;
				//this is the match we want to remove, so don't add it
			}
			else
				returnJa.put(MiscJsonHelpers.matchToJson(subbedMatches.get(i)));
		}

		writeSubbed(c, returnJa, filename);
		return removeMatchSubscriptionToServer(MiscNetworkingHelpers.regId, Integer.toString(m.getId()), b);
	}

	@SuppressWarnings("serial")
	private void addMatchSubscriptionToServer(final String regId, final String matchEntryId, Button b)
	{
		AddMatchSubscriptionTask task = new AddMatchSubscriptionTask();
		task.setRegId(regId);
		task.setButton(b);
		task.setMatchEntryId(matchEntryId);
		Timer t = new Timer("AddMatchSubscriptionTimer");
		t.schedule(task, Calendar.getInstance().getTime());
	}

	private boolean removeMatchSubscriptionToServer(final String regId, final String matchEntryId, Button b)
	{
		SubscriptionRetriever sr = new SubscriptionRetriever();
		JSONArray subs = sr.retrieve(sr.getURI());
		for(int i = 0; i < subs.length(); i++)
		{
			try {
				if(subs != null)
				{
					JSONObject obj = (JSONObject) subs.get(i);

					if(obj != null)
					{
						String matchEntryIdRetrieved = Integer.toString(obj.getInt("model_id")).toLowerCase();
						if(matchEntryIdRetrieved != "null")
						{
							if(matchEntryIdRetrieved.equals(matchEntryId))
							{
								RemoveMatchSubscriptionFromServerTask task = new RemoveMatchSubscriptionFromServerTask();
								task.setButton(b);
								task.setRegId(regId);
								task.setJsonMatchObject(obj);
								Timer t = new Timer("RemoveMatchSubscriptionFromServerTimer");
								t.schedule(task, Calendar.getInstance().getTime());
							}
						}
					}
					else
					{
						Log.e("removeMatchSub", "unable to remove sub, connection to server not available");
						return false;
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

}