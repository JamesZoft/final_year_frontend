package com.james.erebus.core;

public class Match {
	
	private String player1 = "";
	public String getPlayer1() {
		return player1;
	}
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	private String player2 = "";
	public String getPlayer2() {
		return player2;
	}
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	private String parentTourny = "";
	public String getParentTourny() {
		return parentTourny;
	}
	public void setParentTourny(String parentTourny) {
		this.parentTourny = parentTourny;
	}
	private String date = "";
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	private String links = "";
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	private String time = "";
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	private boolean subbed;
	public boolean isSubbed()
	{
		return subbed;
	}
	public void setSubbed(boolean isSubbed)
	{
		subbed = isSubbed;
	}
	
	private String status;
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	private int id;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public boolean equalsMatch(Match m)
	{
		return (id == m.getId());
	}
	
	public boolean isDifferentTo(Match m)
	{
		if(!player1.equals(m.getPlayer1()) || !player2.equals(m.getPlayer2()) || !parentTourny.equals(m.getParentTourny())
				|| !date.equals(m.getDate()) || !links.equals(m.getLinks()) || !time.equals(m.getTime()))
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return "|Id: " + id + "|Time: " + time + "|status: " + status + "|player1: " + player1 + "|player2: " + player2 + "|parenttourny: "
				+ parentTourny + "|links: " + links + "|date: " + date;
	}

}