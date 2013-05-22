package com.james.erebus;


import java.util.Calendar;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.android.gcm.GCMBaseIntentService;
import com.james.erebus.core.Match;
import com.james.erebus.core.Notification;
import com.james.erebus.core.Tournament;
import com.james.erebus.misc.AppConsts;
import com.james.erebus.networking.AddDeviceTask;
import com.james.erebus.networking.MiscNetworkingHelpers;
import com.james.erebus.networking.NotificationManager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * 
 * @author james
 * This class provides methods to deal with GCM messaging and registering all of the methods in this class are
 * triggered by events and are never called by code in the project
 */
public class GCMIntentService extends GCMBaseIntentService{

	public GCMIntentService()
	{
		super("585651294813");
	}

	@Override
	protected void onError(Context context, String errorId) {
		Log.v("onError", "error!");

	}


	/**
	 * Used to convert a GCM notification to a {@link com.james.erebus.core.Match}
	 * @param contents The contents of the GCM notification
	 * @return A match built from the contents of the GCM notification
	 */
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

	/**
	 * 
	 * @param contents the contents of the notification
	 * @return A {@link com.james.erebus.core.Tournament} built from the contents of the notification
	 */
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
			NotificationManager.addNotification(new Notification("The match " + m.getPlayer1() + " vs " + m.getPlayer2() + 
					" has changed. Press 'view' to view it, or 'clear' to clear this notification", m));
		}
		else if(message.contains("new_tournament_data_available"))
		{
			Tournament t = convertGcmNotificationToTournament(message);
			Log.v("converted tourny", t.toString());
			NotificationManager.addNotification(new Notification("The tournament " + t.getName() + " has changed. " +
					"Press 'view' to view it, or 'clear' to clear this notification", t));
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
		AddDeviceTask.setContext(AppConsts.currentActivity);
		task.setRegId(regId);
		Timer t = new Timer("AddDeviceTimer");
		t.schedule(task, Calendar.getInstance().getTime());
	}
}
