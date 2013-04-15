package com.james.erebus.networking;

import java.util.ArrayList;

import com.james.erebus.core.Match;
import com.james.erebus.core.Notification;
import com.james.erebus.core.Tournament;

public abstract class NotificationManager {
	
	private static ArrayList<Match> changedMatches;
	private static ArrayList<Tournament> changedTournaments;

	private static ArrayList<Notification> notifications;
	
	public static ArrayList<Match> getchangedMatches()
	{
		return NotificationManager.changedMatches;
	}
	
	public static ArrayList<Notification> getNotifcations()
	{
		return NotificationManager.notifications;
	}
	
	public static void removeNotification(Notification n)
	{
		if(NotificationManager.notifications == null)
		{
			return;
		}
		ArrayList<Notification> newNotifications = new ArrayList<Notification>();
		for(int i = 0; i < NotificationManager.notifications.size(); i++) // for each stored notification
		{
			if(!n.equalsNotification(NotificationManager.notifications.get(i))) // if this is not the notification we want to remove
			{
				newNotifications.add(NotificationManager.notifications.get(i)); // add it to the new list
				
			}
			else
			{
				if(n.tournyOrMatch() == 0) // 
				{
					ArrayList<Match> newChangedMatches = new ArrayList<Match>();
					for(Match m : NotificationManager.changedMatches)
					{
						if(!m.equalsMatch(n.getMatch()))
							newChangedMatches.add(m);
					}
					NotificationManager.setChangedMatches(newChangedMatches);
				}
				else if(n.tournyOrMatch() == 1)
				{
					ArrayList<Tournament> newChangedTournaments = new ArrayList<Tournament>();
					for(Tournament t : NotificationManager.changedTournaments)
					{
						if(!t.equalsTournament(n.getTournament()))
							newChangedTournaments.add(t);
					}
					NotificationManager.setChangedTournaments(newChangedTournaments);
				}
				else
					throw new IllegalStateException("Notification has no match or tourny in removeNotification");
			}
		}
		NotificationManager.notifications = newNotifications;
	}
	
	public static void setChangedTournaments(ArrayList<Tournament> changedTournaments)
	{
		NotificationManager.changedTournaments = changedTournaments;
	}
	
	public static ArrayList<Tournament> getChangedTournaments()
	{
		return NotificationManager.changedTournaments;
	}
	
	public static void setChangedMatches(ArrayList<Match> changedMatches)
	{
		NotificationManager.changedMatches = changedMatches;
	}
	
	public static void matchesAndTournysToNotifications()
	{
		if(NotificationManager.notifications == null)
			NotificationManager.notifications = new ArrayList<Notification>();
		if(NotificationManager.changedMatches != null && !NotificationManager.changedMatches.isEmpty())
		{
			for(Match m : NotificationManager.changedMatches)
			{
				Notification notif = new Notification("The match " + m.getPlayer1() + " vs " + m.getPlayer2() + 
						" has changed. Press 'view' to view it, or 'clear' to clear this notification", m);
				ArrayList<Notification> notifsCopy = new ArrayList<Notification> (NotificationManager.notifications);
				boolean shouldAdd = true;
				for(Notification n : notifsCopy)
				{
					if(notif.equalsNotification(n))
						shouldAdd = false;
				}
				if(shouldAdd)
					NotificationManager.notifications.add(notif);
				//if(NotificationManager.notifications.isEmpty())
				//	NotificationManager.notifications.add(notif);
			}
		}
		if(NotificationManager.changedTournaments != null && !NotificationManager.changedTournaments.isEmpty())
		{
			for(Tournament t : NotificationManager.changedTournaments)
			{
				Notification notif = new Notification("The tournament " + t.getName() + " has changed. " +
						"Press 'view' to view it, or 'clear' to clear this notification", t);
				ArrayList<Notification> notifsCopy = new ArrayList<Notification> (NotificationManager.notifications);
				boolean shouldAdd = true;
				for(Notification n : notifsCopy)
				{
					if(notif.equalsNotification(n))
						shouldAdd = false;
				}
				if(shouldAdd)
					NotificationManager.notifications.add(notif);
				//if(NotificationManager.notifications.isEmpty())
				//	NotificationManager.notifications.add(notif);
			}
		}
	}

}
