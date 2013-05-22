package com.james.erebus.core;

/**
 * A class that represents a Tournament
 * @author james
 *
 */

public class Tournament {
	
	private String entryReqs;
	private String format;
	private String links;
	private String location;
	private String name;
	private String prizes;
	private String sponsor;
	private String startDate;
	private String ongoing;
	private String past;
	private String future;
	private int id;
	
	public Tournament(String entryReqs, String format, String links, String location, String name,
	 String prizes, String sponsor, String startDate,
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
	
	/**
	 * @return the entryReqs
	 */
	public String getEntryReqs() {
		return entryReqs;
	}
	/**
	 * @param entryReqs the entryReqs to set
	 */
	public void setEntryReqs(String entryReqs) {
		this.entryReqs = entryReqs;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the links
	 */
	public String getLinks() {
		return links;
	}
	/**
	 * @param links the links to set
	 */
	public void setLinks(String links) {
		this.links = links;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the prizes
	 */
	public String getPrizes() {
		return prizes;
	}
	/**
	 * @param prizes the prizes to set
	 */
	public void setPrizes(String prizes) {
		this.prizes = prizes;
	}
	/**
	 * @return the sponsor
	 */
	public String getSponsor() {
		return sponsor;
	}
	/**
	 * @param sponsor the sponsor to set
	 */
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the ongoing
	 */
	public String getOngoing() {
		return ongoing;
	}
	/**
	 * @param ongoing the ongoing to set
	 */
	public void setOngoing(String ongoing) {
		this.ongoing = ongoing;
	}
	/**
	 * @return the past
	 */
	public String getPast() {
		return past;
	}
	/**
	 * @param past the past to set
	 */
	public void setPast(String past) {
		this.past = past;
	}
	/**
	 * @return the future
	 */
	public String getFuture() {
		return future;
	}
	/**
	 * @param future the future to set
	 */
	public void setFuture(String future) {
		this.future = future;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @param t The {@link com.james.erebus.core.Tournament} to compare to
	 * @return If the tournaments are equal in terms of their ID
	 */
	public boolean equalsTournament(Tournament t)
	{
		return (id == t.getId());
	}
	
	/**
	 * 
	 * @param t The {@link com.james.erebus.core.Tournament} to compare to
	 * @return If the tournament is different
	 */
	public boolean isDifferentTo(Tournament t)
	{
		if (  !entryReqs.equals(t.getEntryReqs()) || !format.equals(t.getFormat()) || 
				!location.equals(t.getLocation()) || !name.equals(t.getName()) || !prizes.equals(t.getPrizes()) ||
				!sponsor.equals(t.getSponsor()) || !startDate.equals(t.getStartDate()) || !future.equals(t.getFuture())
				|| !past.equals(t.getPast()) || !ongoing.equals(t.getOngoing()) )
			return true;
		return false;
	}
	
	/**
	 * @return A string representation of the {@link com.james.erebus.core.Tournament} object
	 */
	@Override
	public String toString()
	{
		return "Name: " + name + "|Prize(s): " + prizes + "|Start date: " + startDate 
				 + "|Format: " + format + "|Entry requirements: " + entryReqs + "|Location: " + location
				 + "|Sponsor: " +  sponsor + "|Links: " + links + "|Ongoing: " + ongoing + "|Past: " + past + "|Future: " + future;
	}

}