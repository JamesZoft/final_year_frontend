package com.james.erebus;

public class Tournament {
	
	private String entryReqs;
	private String format;
	private String links;
	private String location;
	private String name;
	private String prizes;
	private String sponsor;
	private String startDate;
	private String status;
	
	public Tournament(String entryReqs, String format, String links, String location, String name,
	 String prizes, String sponsor, String startDate, String status)
	{
		this.entryReqs = entryReqs;
		this.format = format;
		this.links = links;
		this.location = location;
		this.name = name;
		this.prizes = prizes;
		this.sponsor = sponsor;
		this.startDate = startDate;
		this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		return "Name: " + name + "|Prize(s): " + prizes + "|Start date: " + startDate + "|Status :" + status
				 + "|Format: " + format + "|Entry requirements: " + entryReqs + "|Location: " + location
				 + "|Sponsor: " +  sponsor + "|Links: " + links;
	}

}
