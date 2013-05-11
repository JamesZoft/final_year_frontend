package com.james.erebus.core;

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
	 * @return 0 if this notification holds a match, 1 if a tournament, and 2 if nothing
	 */
	public int tournyOrMatch()
	{
		if(m != null)
			return 0;
		else if(t != null)
			return 1;
		return 2;
	}
	
	public Tournament getTournament()
	{
		if(t != null)
			return t;
		return null;
	}
	
	public Match getMatch()
	{
		if(m != null)
			return m;
		return null;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public boolean equalsNotification(Notification n)
	{
		return(text.equals(n.getText()));
	}

}