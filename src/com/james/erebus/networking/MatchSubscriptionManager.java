package com.james.erebus.networking;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

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
	
	
	public boolean subToMatch(Match m, Context c) throws IOException, JSONException
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
		addMatchSubscriptionToServer(MiscNetworkingHelpers.regId, Integer.toString(m.getId()));
		writeSubbed(c, ja, filename);
		return false;
	}
	
	public boolean unsubFromMatch(Context c, Match m)
	{
		boolean retVal = false;
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
				retVal = true;
				//this is the match we want to remove, so don't add it
			}
			else
				returnJa.put(MiscJsonHelpers.matchToJson(subbedMatches.get(i)));
		}
		
		writeSubbed(c, returnJa, filename);
		return retVal;
	}
	
	@SuppressWarnings("serial")
	private void addMatchSubscriptionToServer(final String regId, final String matchEntryId)
	{
		Log.v("addmatchsubtoserver", regId);
		Log.v("addmatchsubtoserver", matchEntryId);
		MiscNetworkingHelpers.sendInformationToServer(regId, "subscriptions.json", 
				new ArrayList<BasicNameValuePair>() {{ 
					add(new BasicNameValuePair("subscription[device_id]", regId));
					add(new BasicNameValuePair("subscription[match_entry_id]", matchEntryId));
					}});
	}

}