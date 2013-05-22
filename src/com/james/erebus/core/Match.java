package com.james.erebus.core;


/**
 * A class to represent a Match
 * @author james
 *
 */

public class Match {

	private String player1 = "";
	private String player2 = "";
	private String parentTourny = "";
	private String date = "";
	private String links = "";
	private String time = "";
	private String status;
	private int id;

	/**
	 * @return the player1
	 */
	public String getPlayer1() {
		return player1;
	}

	/**
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	/**
	 * @return the player2
	 */
	public String getPlayer2() {
		return player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	/**
	 * @return the parentTourny
	 */
	public String getParentTourny() {
		return parentTourny;
	}

	/**
	 * @param parentTourny the parentTourny to set
	 */
	public void setParentTourny(String parentTourny) {
		this.parentTourny = parentTourny;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @param m The {@link com.james.erebus.core.Match} to compare to
	 * @return If the matches are equal in terms of their ID
	 */
	public boolean equalsMatch(Match m)
	{
		return (id == m.getId());
	}
	
	/**
	 * 
	 * @param m The {@link com.james.erebus.core.Match} to compare to
	 * @return If the match is different
	 */
	public boolean isDifferentTo(Match m)
	{
		if(!player1.equals(m.getPlayer1()) || !player2.equals(m.getPlayer2()) || !parentTourny.equals(m.getParentTourny())
				|| !date.equals(m.getDate()) || !links.equals(m.getLinks()) || !time.equals(m.getTime()))
			return true;
		return false;
	}

	/**
	 * @return A string representation of the {@link com.james.erebus.core.Match} object
	 */
	@Override
	public String toString()
	{
		return "|Id: " + id + "|Time: " + time + "|status: " + status + "|player1: " + player1 + "|player2: " + player2 + "|parenttourny: "
				+ parentTourny + "|links: " + links + "|date: " + date;
	}

}