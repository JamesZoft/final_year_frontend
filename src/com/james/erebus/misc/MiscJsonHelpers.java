package com.james.erebus.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.core.Match;
import com.james.erebus.core.Tournament;

public class MiscJsonHelpers {

	
	/**
	 * @param args
	 */
	public static String getValuesFromJsonObject(JSONObject o)
	{
		String obj = o.toString();
		String[] values = obj.split(",");
		Pattern patt = Pattern.compile(".*:");
		obj = "";
		for(int i = 0; i < values.length; i++)
		{
			Matcher m = patt.matcher(values[i]);
			values[i] = m.replaceFirst(",");
			obj += values [i];
		}
		patt = Pattern.compile("\""); //get rid of extraneous quote marks and brackets
		Matcher m = patt.matcher(obj);
		obj = m.replaceAll("");
		patt = Pattern.compile(Pattern.quote("}"));
		Matcher m2 = patt.matcher(obj);
		obj = m2.replaceAll("");
		patt = Pattern.compile(",");
		Matcher m3 = patt.matcher(obj);
		obj = m3.replaceFirst("");
		return obj;
	}
	
	public static JSONObject matchToJson(Match m)
	{
		JSONObject match = new JSONObject();
		try {
			match.put("date", m.getDate());
			match.put("links", m.getLinks());
			match.put("parentTournament", m.getParentTourny());
			match.put("player1", m.getPlayer1());
			match.put("player2", m.getPlayer2());
			match.put("status", m.getStatus());
			match.put("time", m.getTime());
			match.put("id", m.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return match;
	}
	
	public static List<Tournament> jsonTournamentArrayToTournamentList(JSONArray ja)
	{
		ArrayList<Tournament> updatedTournaments = new ArrayList<Tournament>();
		for(int i = 0; i < ja.length(); i++)
		{
			try {
				updatedTournaments.add(jsonToTournament((JSONObject) ja.get(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return updatedTournaments;
	}
	
	public static List<Match> jsonMatchArrayToMatchList(JSONArray ja)
	{
		ArrayList<Match> updatedMatches = new ArrayList<Match>();
		for(int i = 0; i < ja.length(); i++)
		{
			try {
				updatedMatches.add(jsonToMatch((JSONObject) ja.get(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return updatedMatches;
	}
	
	public static Match jsonToMatch(JSONObject obj)
	{
		Match match = new Match();

		try{
			match.setPlayer1(obj.getString("player1"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for player1");
		}
		try{
			match.setPlayer2(obj.getString("player2"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for player2");
		}
		try{
			match.setParentTourny(obj.getString("parentTournament"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for parentTournament");
		}
		try{
			match.setDate(obj.getString("date"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for date");
		}
		try{
			match.setLinks(obj.getString("links"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for links");
		}
		try{
			match.setStatus(obj.getString("status"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for status");
		}
		try{
			match.setId(obj.getInt("id"));
		}
		catch(JSONException e)
		{
			System.err.println("WARNING: no value for id");
		}
		try{
			String entry = obj.getString("time");
			Pattern patt = Pattern.compile(".*T");
			Matcher m = patt.matcher(entry);
			entry = m.replaceAll("");
			patt = Pattern.compile("Z");
			m = patt.matcher(entry);
			entry = m.replaceAll("");
			match.setTime(entry);
		}
		catch(JSONException e)
		{
			System.out.println("No value for time");
		}

		return match;

	}
	
	public static JSONObject tournamentToJson(Tournament t)
	{
		JSONObject tournament = new JSONObject();
		try {
			tournament.put("start_date", t.getStartDate());
			tournament.put("links", t.getLinks());
			tournament.put("status", t.getStatus());
			tournament.put("entry_reqs", t.getEntryReqs());
			tournament.put("format", t.getFormat());
			tournament.put("future", t.getFuture());
			tournament.put("past", t.getPast());
			tournament.put("ongoing", t.getOngoing());
			tournament.put("location", t.getLocation());
			tournament.put("name", t.getName());
			tournament.put("prizes", t.getPrizes());
			tournament.put("sponsor", t.getSponsor());
			tournament.put("id", t.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tournament;
	}
	
	
	
	public static Tournament jsonToTournament(JSONObject obj)
	{
		Tournament tournament = new Tournament();

		try{
			tournament.setEntryReqs(obj.getString("entry_reqs"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for entry_reqs");
		}
		try{
			tournament.setFormat(obj.getString("format"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for format");
		}
		try{
			tournament.setFuture(obj.get("future").toString());
		}
		catch(JSONException e)
		{
			System.out.println("No value for future");
		}
		try{
			tournament.setLocation(obj.getString("location"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for location");
		}
		try{
			tournament.setLinks(obj.getString("links"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for links");
		}
		try{
			tournament.setName(obj.getString("name"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for name");
		}
		try{
			tournament.setOngoing(obj.get("ongoing").toString());
		}
		catch(JSONException e)
		{
			System.out.println("No value for ongoing");
		}
		try{
			tournament.setPast(obj.get("past").toString());
		}
		catch(JSONException e)
		{
			System.out.println("No value for past");
		}
		try{
			tournament.setPrizes(obj.getString("prizes"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for prizes");
		}
		try{
			tournament.setSponsor(obj.getString("sponsor"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for sponsor");
		}
		try{
			tournament.setStartDate(obj.getString("start_date"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for start_date");
		}
		try{
			tournament.setId(obj.getInt("id"));
		}
		catch(JSONException e)
		{
			System.err.println("WARNING: No value for id");
		}
		return tournament;

	}
}