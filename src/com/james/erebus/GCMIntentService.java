package com.james.erebus;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.android.gcm.GCMBaseIntentService;
import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.core.Match;
import com.james.erebus.core.Tournament;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.AddDeviceTask;
import com.james.erebus.networking.MatchRetriever;
import com.james.erebus.networking.MatchSubscriptionManager;
import com.james.erebus.networking.MiscNetworkingHelpers;
import com.james.erebus.networking.NotificationManager;
import com.james.erebus.networking.TournamentRetriever;
import com.james.erebus.networking.TournamentSubscriptionManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class GCMIntentService extends GCMBaseIntentService{

	private String uri = "http://teamfrag.net:3002/";

	public GCMIntentService()
	{
		super("585651294813");
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.v("onError", "error!");

	}


	private Match convertGcmNotificationToMatch(String contents)
	{
		String[] values = contents.split(",\"");
		Match m = new Match();
		for(int i = 0; i < values.length; i++)
		{
			String value = values[i];
			
			Pattern patt = Pattern.compile(".*\\{");
			Matcher matcher = patt.matcher(value);
			value = matcher.replaceAll("");;
			patt = Pattern.compile("\\}.*");
			matcher = patt.matcher(value);
			value = matcher.replaceAll("");
			String[] keyvalue = value.split("\":");
			patt = Pattern.compile("\"");
			matcher = patt.matcher(keyvalue[0]);
			keyvalue[0] = matcher.replaceAll("");
			matcher = patt.matcher(keyvalue[1]);
			keyvalue[1] = matcher.replaceAll("");
			
			if(keyvalue[0].equals("date"))
				m.setDate(keyvalue[1]);
			if(keyvalue[0].equals("id"))
				m.setId(Integer.parseInt(keyvalue[1]));
			if(keyvalue[0].equals("links"))
				m.setLinks(keyvalue[1]);	
			if(keyvalue[0].equals("parentTournament"))
				m.setParentTourny(keyvalue[1]);
			if(keyvalue[0].equals("player1"))
				m.setPlayer1(keyvalue[1]);
			if(keyvalue[0].equals("player2"))
				m.setPlayer2(keyvalue[1]);
			if(keyvalue[0].equals("status"))
				m.setStatus(keyvalue[1]);
			if(keyvalue[0].equals("time"))
				m.setTime(keyvalue[1]);		
		}
		Log.v("match", m.toString());
		return m;
	}

	private Tournament convertGcmNotificationToTournament(String contents)
	{
		String[] values = contents.split(",\"");
		Tournament t = new Tournament();
		for(int i = 0; i < values.length; i++)
		{
			String value = values[i];
			
			Pattern patt = Pattern.compile(".*\\{");
			Matcher m = patt.matcher(value);
			value = m.replaceAll("");;
			patt = Pattern.compile("\\}.*");
			m = patt.matcher(value);
			value = m.replaceAll("");
			String[] keyvalue = value.split("\":");
			patt = Pattern.compile("\"");
			m = patt.matcher(keyvalue[0]);
			keyvalue[0] = m.replaceAll("");
			m = patt.matcher(keyvalue[1]);
			keyvalue[1] = m.replaceAll("");
			if(keyvalue[0].equals("entry_reqs"))
				t.setEntryReqs(keyvalue[1]);
			if(keyvalue[0].equals("format"))
				t.setFormat(keyvalue[1]);
			if(keyvalue[0].equals("future"))
				t.setFuture(keyvalue[1]);	
			if(keyvalue[0].equals("id"))
				t.setId(Integer.parseInt(keyvalue[1]));
			if(keyvalue[0].equals("links"))
				t.setLinks(keyvalue[1]);
			if(keyvalue[0].equals("location"))
				t.setLocation(keyvalue[1]);
			if(keyvalue[0].equals("name"))
				t.setName(keyvalue[1]);
			if(keyvalue[0].equals("ongoing"))
				t.setOngoing(keyvalue[1]);
			if(keyvalue[0].equals("past"))
				t.setPast(keyvalue[1]);
			if(keyvalue[0].equals("prizes"))
				t.setPrizes(keyvalue[1]);
			if(keyvalue[0].equals("sponsor"))
				t.setSponsor(keyvalue[1]);
			if(keyvalue[0].equals("start_date"))
				t.setStartDate(keyvalue[1]);			
		}
		Log.v("tournament", t.toString());
		return t;
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String message = intent.getExtras().toString();
		Log.v("onMessage", "content: " + message);
		
		if(message.contains("new_match_data_available"))
		{
			Match m = convertGcmNotificationToMatch(message);
			Log.v("converted match", m.toString());
			MatchRetriever mr = new MatchRetriever();
			MiscNetworkingHelpers.addEntryToInternalStorage(MiscJsonHelpers.matchToJson(m), mr.getMatchesFilename());
			MatchSubscriptionManager msm = new MatchSubscriptionManager();
			ArrayList<Match> newMatches = msm.compareSubbedMatches(this);
			if(newMatches != null && !newMatches.isEmpty())
			{
				NotificationManager.setChangedMatches(newMatches);
			}
		}
		else if(message.contains("new_tournament_data_available"))
		{
			Tournament t = convertGcmNotificationToTournament(message);
			Log.v("converted tourny", t.toString());
			TournamentRetriever tr = new TournamentRetriever();
			MiscNetworkingHelpers.addEntryToInternalStorage(MiscJsonHelpers.tournamentToJson(t), tr.getTournamentsFilename());
			TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
			ArrayList<Tournament> newTournaments = tsm.compareSubbedTournaments(this);
			if(newTournaments != null && !newTournaments.isEmpty())
			{
				NotificationManager.setChangedTournaments(newTournaments);
			}
		}
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.v("onUnregistered", "unregistered now!");

	}

	@Override
	protected void onRegistered(Context context, String regId) {
		//send reg id to server
		Log.v("onRegistered", "send regId to server");
		addDeviceToServer(regId);
		MiscNetworkingHelpers.regId = regId;
	}


	private void addDeviceToServer(final String regId)
	{
		AddDeviceTask task = new AddDeviceTask();
		task.setRegId(regId);
		Timer t = new Timer("AddDeviceTimer");
		t.schedule(task, Calendar.getInstance().getTime());
	}
}
