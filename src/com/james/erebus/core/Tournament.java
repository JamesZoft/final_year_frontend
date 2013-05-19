package com.james.erebus.core;

public class Tournament {
	
	private String entryReqs;
	private String format;
	private String links;
	private String location;
	private String name;
	private String prizes;
	private String sponsor;
	private String startDate;
	private String subscribed;
	private String ongoing;
	private String past;
	private String future;
	
	public Tournament(String entryReqs, String format, String links, String location, String name,
	 String prizes, String sponsor, String startDate, String subscribed,
	 String ongoing, String future, String past)
	{
		this.entryReqs = entryReqs;
		this.format = format;
		this.links = links;
		this.location = location;
		this.name = name;
		this.prizes = prizes;
		this.sponsor = sponsor;
		this.startDate = startDate;
		this.subscribed = subscribed;
		this.ongoing = ongoing;
		this.past = past;
		this.future = future;
	}
	public Tournament()
	{
		entryReqs = "";
		format = "";
		links = "";
		location = "";
		name = "";
		prizes = "";
		sponsor = "";
		startDate = "";
		subscribed = "";
		ongoing = "";
		past = "";
		future = "";
	}
	
	public String getStatus()
	{
		String status = "Unknown";
		if(ongoing.equals("true"))
			status = "Ongoing";
		else if(past.equals("true"))
			status = "Finished";
		else if(future.equals("true"))
			status = "Not started";
		return status;
	}
	
	public String getFuture()
	{
		return future;
	}
	
	public void setFuture(String future)
	{
		this.future = future;
	}
	
	public String getPast()
	{
		return past;
	}
	
	public void setPast(String past)
	{
		this.past = past;
	}
	
	public String getOngoing()
	{
		return ongoing;
	}
	
	public void setOngoing(String ongoing)
	{
		this.ongoing = ongoing;
	}
	
	public String getSubscribed()
	{
		return subscribed;
	}
	
	public void setSubscribed(String subscribed)
	{
		this.subscribed = subscribed;
	}
	
	public String getEntryReqs() {
		return entryReqs;
	}

	public void setEntryReqs(String entryReqs) {
		this.entryReqs = entryReqs;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrizes() {
		return prizes;
	}

	public void setPrizes(String prizes) {
		this.prizes = prizes;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
	
	public boolean equalsTournament(Tournament t)
	{
		return (id == t.getId());
	}
	
	public boolean isDifferentTo(Tournament t)
	{
		if (  !entryReqs.equals(t.getEntryReqs()) || !format.equals(t.getFormat()) || 
				!location.equals(t.getLocation()) || !name.equals(t.getName()) || !prizes.equals(t.getPrizes()) ||
				!sponsor.equals(t.getSponsor()) || !startDate.equals(t.getStartDate()) || !future.equals(t.getFuture())
				|| !past.equals(t.getPast()) || !ongoing.equals(t.getOngoing()) )
			return true;
		return false;
	}

	
	
	/*
	 * 
	private String entryReqs;
	private String format;
	private String links;
	private String location;
	private String name;
	private String prizes;
	private String sponsor;
	private String startDate;
	private String status;
	 */
	
	public String toString()
	{
		return "Name: " + name + "|Prize(s): " + prizes + "|Start date: " + startDate 
				 + "|Format: " + format + "|Entry requirements: " + entryReqs + "|Location: " + location
				 + "|Sponsor: " +  sponsor + "|Links: " + links + "|Ongoing: " + ongoing + "|Past: " + past + "|Future: " + future;
	}

}