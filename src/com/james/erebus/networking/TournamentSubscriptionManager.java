package com.james.erebus.networking;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.core.Tournament;
import com.james.erebus.misc.MiscJsonHelpers;

public class TournamentSubscriptionManager extends SubscriptionManager {
	
	private static final String filename = "subbedTournaments.json";

	public boolean isTournamentSubbed(Context c, Tournament t)
	{
		ArrayList<Tournament> tournaments = getSubbedTournaments(c);
		for(Tournament tournament : tournaments)
		{
			if(t.equalsTournament(tournament))
				return true;
		}
		return false;
	}

	public ArrayList<Tournament> getSubbedTournaments(Context c)
	{
		JSONArray ja = readSubbed(c, "subbedTournaments.json");
		if(ja != null)
		{
			ArrayList<Tournament> tournaments = new ArrayList<Tournament>();
			for(int i = 0; i < ja.length(); i++)
			{
				try {
					tournaments.add(MiscJsonHelpers.jsonToTournament((JSONObject) ja.get(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return tournaments;
		}
		else
		{
			return new ArrayList<Tournament>();
		}
	}
	
	
	public boolean subToTournament(Tournament t, Context c) throws IOException, JSONException
	{
		JSONArray ja = readSubbed(c, "subbedTournaments.json");
		if(ja != null)
		{
			ArrayList<Tournament> subbedTournaments = new ArrayList<Tournament>();
			for(int i = 0; i < ja.length(); i++)
			{
				try {
					subbedTournaments.add(MiscJsonHelpers.jsonToTournament((JSONObject) ja.get(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			for(int i = 0; i < subbedTournaments.size(); i++)
			{
				if(subbedTournaments.get(i).equalsTournament(t))
				{
					return false;
				}
			}
		}
		else
			ja = new JSONArray();
		ja.put(MiscJsonHelpers.tournamentToJson(t));
		writeSubbed(c, ja, "subbedTournaments.json");
		addTournamentSubscriptionToServer(MiscNetworkingHelpers.regId, Integer.toString(t.getId()));
		return false;
	}
	
	/**
	 * 
	 * @param c - The context in which this method is invoked
	 * @return - The ArrayList of updated tournaments if it's newer, else null
	 */
	public ArrayList<Tournament> compareSubbedTournaments(Context c)
	{
		ArrayList<Tournament> subbedTournaments = getSubbedTournaments(c);
		TournamentRetriever tr = new TournamentRetriever();
		JSONArray ja = tr.retrieve(tr.getURI());
		if(ja == null)
			return null;
		ArrayList<Tournament> updatedTournaments = (ArrayList<Tournament>) MiscJsonHelpers.jsonTournamentArrayToTournamentList(ja);
		ArrayList<Tournament> changedTournaments = new ArrayList<Tournament>();
		JSONArray newSubbedJa = new JSONArray();
		boolean shouldAdd;
		for(Tournament t : subbedTournaments)
		{
			shouldAdd = true;
			for(Tournament t2 : updatedTournaments)
			{
				if(t.equalsTournament(t2))
				{
					if(t.isDifferentTo(t2))
					{
						Log.i("l54:tournamentsubmanager", "added changed tournament");
						changedTournaments.add(t2);
						newSubbedJa.put(MiscJsonHelpers.tournamentToJson(t2));
						shouldAdd = false;
						break;
					}
				}
			}
			if(shouldAdd)
				newSubbedJa.put(MiscJsonHelpers.tournamentToJson(t));
		}
		writeSubbed(c, newSubbedJa, filename);
		return changedTournaments;
	}
	
	public boolean unsubFromTournament(Context c, Tournament t)
	{
		boolean retVal = false;
		JSONArray ja = readSubbed(c, "subbedTournaments.json");
		
		ArrayList<Tournament> subbedTournaments = new ArrayList<Tournament>();
		for(int i = 0; i < ja.length(); i++)
		{
			try {
				subbedTournaments.add(MiscJsonHelpers.jsonToTournament((JSONObject) ja.get(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		JSONArray returnJa = new JSONArray();
		for(int i = 0; i < subbedTournaments.size(); i++)
		{
			if(subbedTournaments.get(i).equalsTournament(t))
			{
				retVal = true;
				//this is the tournament we want to remove, so don't add it
			}
			else
				returnJa.put(MiscJsonHelpers.tournamentToJson(subbedTournaments.get(i)));
		}
		writeSubbed(c, returnJa, "subbedTournaments.json");
		removeTournamentSubscriptionToServer(MiscNetworkingHelpers.regId, Integer.toString(t.getId()));
		return retVal;
	}
	
	@SuppressWarnings("serial")
	private void addTournamentSubscriptionToServer(final String regId, final String tournamentEntryId)
	{
		Log.v("addtournamentsubtoserver", regId);
		Log.v("addtournamentsubtoserver", tournamentEntryId);
		try {
			MiscNetworkingHelpers.postInformationToServer(regId, "subscriptions.json", 
					new ArrayList<BasicNameValuePair>() {{ 
						add(new BasicNameValuePair("subscription[device_registration_id]", regId));
						add(new BasicNameValuePair("subscription[model_id]", tournamentEntryId));
						add(new BasicNameValuePair("subscription[model_type]", "TournamentEntry"));
						}});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void removeTournamentSubscriptionToServer(final String regId, final String tournamentEntryId)
	{
		
		
		SubscriptionRetriever sr = new SubscriptionRetriever();
		JSONArray subs = sr.retrieve(sr.getURI());
		for(int i = 0; i < subs.length(); i++)
		{
			try {
				JSONObject obj = (JSONObject) subs.get(i);
				String tournamentEntryIdRetrieved = Integer.toString(obj.getInt("tournament_entry_id")).toLowerCase();
				if(tournamentEntryIdRetrieved != "null")
					if(tournamentEntryIdRetrieved.equals(tournamentEntryId))
					{
						try {
							MiscNetworkingHelpers.deleteInformationFromServer(regId, "subscriptions/" + Integer.toString(obj.getInt("id")) + ".json");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}