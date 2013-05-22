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
import com.james.erebus.core.Tournament;
import com.james.erebus.misc.MiscJsonHelpers;

/**
 * Child class of {@link com.james.erebus.networking.SubscriptionManager} for managing {@link com.james.erebus.core.Tournament}
 * subscriptions
 * @author james
 *
 */

public class TournamentSubscriptionManager extends SubscriptionManager {

	private static final String filename = "subbedTournaments.json";

	/**
	 * Method to check if a {@link com.james.erebus.core.Tournament} is subscribed to
	 * @param c The {@link android.content.Context} in which this method is invoked
	 * @param t The Tournament being checked
	 * @return True if the Tournament is subscribed to, false if not
	 */
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

	/**
	 * 
	 * @param c The {@link android.content.Context} in which this method is invoked
	 * @return an {@link java.util.ArrayList} of subscribed {@link com.james.erebus.core.Tournament tournaments}
	 */
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

	/**
	 * Subscribes to a {@link com.james.erebus.core.Tournament} both internally and also calls
	 * the method to send this subscription to the server
	 * @param t The Tournament to be subscribed to
	 * @param c The {@link android.content.Context} in which this method is invoked
	 * @param b The {@link android.widget.Button} which was pressed to invoke this method
	 * @return True if the Tournament was subscribed to successfully, false if not
	 * @throws IOException
	 * @throws JSONException
	 */
	public boolean subToTournament(Tournament t, Context c, Button b) throws IOException, JSONException
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
		addTournamentSubscriptionToServer(MiscNetworkingHelpers.regId, Integer.toString(t.getId()), b, t, c);
		return false;
	}

	/**
	 * Compare
	 * @param c - The context in which this method is invoked
	 * @return - The ArrayList of updated tournaments if it's newer, else null
	 */
	/*
	public ArrayList<Tournament> compareSubbedTournaments(Context c)
	{
		ArrayList<Tournament> subbedTournaments = getSubbedTournaments(c);
		TournamentRetriever tr = new TournamentRetriever();
		JSONArray ja = tr.retrieve(tr.getURI(), tr.getTournamentsFilename());
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
	*/
	
	/**
	 * Unsubscribes from a {@link com.james.erebus.core.Tournament} both internally and also calls
	 * the method to send this subscription to the server
	 * @param c The {@link android.content.Context} in which this method is invoked
	 * @param t The Tournament to be unsubscribed from
	 * @param b The {@link android.widget.Button} which was pressed to invoke this method
	 * @return True if the Tournament was unsubscribed from successfully, false if not
	 */
	public boolean unsubFromTournament(Context c, Tournament t, Button b)
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
		try {
			removeTournamentSubscriptionToServer(MiscNetworkingHelpers.regId, Integer.toString(t.getId()), b);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeSubbed(c, returnJa, "subbedTournaments.json");
		return retVal;
	}

	/**
	 * Adds a {@link com.james.erebus.core.Tournament} subscription to the server
	 * @param regId The registration ID for this app
	 * @param tournamentEntryId The Tournament id
	 * @param b The {@link android.widget.Button} that was pressed to invoke this method
	 * @param tournament The Tournament being subscribed to
	 * @param c The {@link android.content.Context} in which this method was invoked
	 */
	private void addTournamentSubscriptionToServer(final String regId, final String tournamentEntryId, Button b, Tournament tournament, Context c)
	{
		AddTournamentSubscriptionToServerTask task = new AddTournamentSubscriptionToServerTask();
		task.setContext(c);
		task.setTournament(tournament);
		task.setRegId(regId);
		task.setButton(b);
		task.setTournamentEntryId(tournamentEntryId);
		Timer t = new Timer("AddTournamentSubscriptionToServerTimer");
		t.schedule(task, Calendar.getInstance().getTime());
	}

	/**
	 * Removes a {@link com.james.erebus.core.Tournament} subscription from the server
	 * @param regId The registration ID for this app
	 * @param tournamentEntryId The Tournament id
	 * @param b The {@link android.widget.Button} that was pressed to invoke this method
	 * @throws UnknownHostException
	 */
	private void removeTournamentSubscriptionToServer(final String regId, final String tournamentEntryId, Button b) throws UnknownHostException
	{
		SubscriptionRetriever sr = new SubscriptionRetriever();
		JSONArray subs = sr.forceRetrieveFromServer(sr.getURI(), sr.getSubscriptionsFilename());
		if(subs != null)
		{
			for(int i = 0; i < subs.length(); i++)
			{
				try {
					JSONObject obj = (JSONObject) subs.get(i);
					String tournamentEntryIdRetrieved = Integer.toString(obj.getInt("model_id")).toLowerCase();
					if(tournamentEntryIdRetrieved != "null")
					{
						if(tournamentEntryIdRetrieved.equals(tournamentEntryId))
						{
							RemoveTournamentSubscriptionFromServerTask task = new RemoveTournamentSubscriptionFromServerTask();
							task.setButton(b);
							task.setRegId(regId);
							task.setJsonTournamentObject(obj);
							Timer t = new Timer("RemoveTournamentSubscriptionFromServerTimer");
							t.schedule(task, Calendar.getInstance().getTime());
							Log.v("removetournysub", "set task to remove tourny sub");
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}