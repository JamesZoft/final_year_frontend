package com.james.erebus.networking;

import java.util.ArrayList;

import com.james.erebus.core.Notification;

/**
 * This class manages the {@link com.james.erebus.core.Notification} class
 * and provides methods to add, remove and 
 * @author james
 *
 */

public abstract class NotificationManager {
	
	private static ArrayList<Notification> notifications = new ArrayList<Notification>();
	
	/**
	 * 
	 * @return an {@link java.util.ArrayList} of all {@link com.james.erebus.core.Notification notifications}
	 */
	public static ArrayList<Notification> getNotifcations()
	{
		return NotificationManager.notifications;
	}
	
	/**
	 * Removes a {@link com.james.erebus.core.Notification} 
	 * @param n The Notification to be removed
	 */
	public static void removeNotification(Notification n)
	{
		for(Notification notif : NotificationManager.getNotifcations())
		{
			if(n.equalsNotification(notif))
				notifications.remove(notif);
		}
	}
	
	/**
	 * Adds a {@link com.james.erebus.core.Notification}
	 * @param n The Notification to be added
	 */
	public static void addNotification(Notification n)
	{
		NotificationManager.notifications.add(n);
	}
}