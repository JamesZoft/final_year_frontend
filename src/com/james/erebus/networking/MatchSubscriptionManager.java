package com.james.erebus.networking;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.core.Match;
import com.james.erebus.misc.MiscJsonHelpers;

public class MatchSubscriptionManager extends SubscriptionManager {
	
	//ArrayList<Match> subbedMatches;
	
	public MatchSubscriptionManager()
	{
		 //subbedMatches = new ArrayList<Match>();
	}
	
	public boolean isMatchSubbed(Match m)
	{
		ArrayList<Match> matches = getSubbedMatches();
		for(Match match : matches)
		{
			if(m.equals(match))
				return true;
		}
		return false;
	}

	public ArrayList<Match> getSubbedMatches()
	{
		JSONArray ja = readSubbed("subbedMatches.json");
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
		JSONArray ja = readSubbed("subbedMatches.json");
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
				if(subbedMatches.get(i).equals(m))
				{
					return false;
				}
			}
		}
		else
			ja = new JSONArray();
		ja.put(MiscJsonHelpers.matchToJson(m));
		writeSubbed(ja, "subbedMatches.json");
		return false;
	}
	
	public boolean unsubFromMatch(Match m)
	{
		boolean retVal = false;
		JSONArray ja = readSubbed("subbedMatches.json");
		
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
			if(subbedMatches.get(i).equals(m))
			{
				retVal = true;
				//this is the match we want to remove, so don't add it
			}
			else
				returnJa.put(MiscJsonHelpers.matchToJson(subbedMatches.get(i)));
		}
		writeSubbed(returnJa, "subbedMatches.json");
		return retVal;
	}

}
