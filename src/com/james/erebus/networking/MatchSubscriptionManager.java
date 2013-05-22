package com.james.erebus.networking;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;


import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.core.Match;
import com.james.erebus.misc.MiscJsonHelpers;

/**
 * Child class of {@link com.james.erebus.networking.SubscriptionManager} that specifies the filename
 * that subscribed matches get saved to and adds methods to add, remove and get subscribed matches
 * @author james
 *
 */

public class MatchSubscriptionManager extends SubscriptionManager {

	private static final String filename = "subbedMatches.json";

	public MatchSubscriptionManager()
	{
		
	}

	/**
	 * Checks if a {@link com.james.erebus.core.Match} is subscribed to
	 * @param c The {@link android.content.Context} in which this is being asked
	 * @param m The Match to be checked
	 * @return True if the match is subscribed, false if not
	 */
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
	 * @param c - The {@link android.content.Context} in which this method is invoked
	 * @return - The ArrayList of updated matches if it's newer, else null
	 */
	public ArrayList<Match> compareSubbedMatches(Context c)
	{
		ArrayList<Match> subbedMatches = getSubbedMatches(c);
		MatchRetriever mr = new MatchRetriever();
		JSONArray ja = mr.retrieve(mr.getURI(), mr.getMatchesFilename());
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

	/**
	 * Gets all currently subscribed matches
	 * @param c The {@link android.content.Context} in which this is being asked 
	 * @return The {@link java.util.ArrayList} of subscribed {@link com.james.erebus.core.Match}
	 */
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

	/**
	 * Adds the specified {@link com.james.erebus.Match} to the internal list of subscribed matches
	 * @param m the Match to be added
	 * @param c The {@link android.content.Context} in which this is being asked
	 * @param b The subscribe/unsubscribe button of which the text will change
	 * @throws IOException
	 * @throws JSONException
	 */
	public void subToMatch(Match m, Context c, Button b) throws IOException, JSONException
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
					//already subbed
				}
			}
		}
		else
			ja = new JSONArray();
		ja.put(MiscJsonHelpers.matchToJson(m));
		addMatchSubscriptionToServer(MiscNetworkingHelpers.regId, b, m, c);
		writeSubbed(c, ja, filename);
	}

	/**
	 * Removes the specified {@link com.james.erebus.Match} from the internal list of subscribed matches
	 * @param m the Match to be added
	 * @param c The {@link android.content.Context} in which this is being asked
	 * @param b The subscribe/unsubscribe button of which the text will change
	 */
	public void unsubFromMatch(Context c, Match m, Button b)
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
		try {
			removeMatchSubscriptionFromServer(MiscNetworkingHelpers.regId, Integer.toString(m.getId()), b);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeSubbed(c, returnJa, filename);
	}

	/**
	 * Adds a {@link com.james.erebus.core.Match} subscription to the server
	 * @param regId The registration ID of this app
	 * @param b The subscribe/unsubscribe button of which the text will change
	 * @param m the Match to be added
	 * @param c The {@link android.content.Context} in which this is being asked
	 */
	private void addMatchSubscriptionToServer(final String regId, Button b, Match m, Context c)
	{
		AddMatchSubscriptionTask task = new AddMatchSubscriptionTask();
		task.setRegId(regId);
		task.setButton(b);
		task.setMatchEntryId(Integer.toString(m.getId()));
		Timer t = new Timer("AddMatchSubscriptionTimer");
		t.schedule(task, Calendar.getInstance().getTime());
	}

	/**
	 * Removes a {@link com.james.erebus.core.Match} subscription from the server
	 * @param regId The registration ID of this app
	 * @param matchEntryId The ID of this match
	 * @param b The {@link android.widget.Button} which was pressed to invoke this method
	 * @throws UnknownHostException 
	 */
	private void removeMatchSubscriptionFromServer(final String regId, final String matchEntryId, Button b) throws UnknownHostException
	{
		SubscriptionRetriever sr = new SubscriptionRetriever();
		JSONArray subs = sr.forceRetrieveFromServer(sr.getURI(), sr.getSubscriptionsFilename());
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
						//return false;
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}