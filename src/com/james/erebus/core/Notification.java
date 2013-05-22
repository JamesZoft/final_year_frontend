package com.james.erebus.core;

/**
 * Class that represents a notification
 * @author james
 *
 */

public class Notification {

	String text;
	Match m;
	Tournament t;
	
	public Notification(String text, Match m)
	{
		this.text = text;
		this.m = m;
	}
	
	public Notification(String text, Tournament t)
	{
		this.text = text;
		this.t = t;
	}
	
	/**
	 * 
	 * @return 0 if this notification holds a {@link com.james.erebus.core.Match}, 1 if a 
	 * {@link com.james.erebus.core.Tournament}, and 2 if nothing
	 */
	public int tournyOrMatch()
	{
		if(m != null)
			return 0;
		else if(t != null)
			return 1;
		return 2;
	}
	
	/**
	 * 
	 * @return The tournament in this notification, if there is one
	 */
	public Tournament getTournament()
	{
		if(t != null)
			return t;
		return null;
	}
	
	/**
	 * 
	 * @return The match in this notification, if there is one
	 */
	public Match getMatch()
	{
		if(m != null)
			return m;
		return null;
	}
	
	/**
	 * 
	 * @return The text in this notification
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * 
	 * @param text The text to set the notification text to
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	
	/**
	 * 
	 * @param n The {@link com.james.erebus.core.Notification} to compare to
	 * @return True if the notification is equal to this Notification, else false
	 */
	public boolean equalsNotification(Notification n)
	{
		if(m != null)
		{
			if(n.getMatch() != null)
			{
				if(m.equalsMatch(n.getMatch()))
				{
					if(n.getText().equals(text))
						return true;
				}
			}
		}
		else if(t != null)
		{
			if(n.getTournament() != null)
			{
				if(t.equalsTournament(n.getTournament()))
				{
					if(n.getText().equals(text))
						return true;
				}
			}
		}
		return false;
	}

}