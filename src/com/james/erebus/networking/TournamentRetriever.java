package com.james.erebus.networking;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Child class of {@link com.james.erebus.networking.Retriever} for retrieving {@link com.james.erebus.core.Tournament tournaments}
 * @author james
 *
 */

public class TournamentRetriever extends Retriever{
	String tournamentsFilename;
	URI uri;
	
	public TournamentRetriever()
	{
		try {
			uri = new URI("http://teamfrag.net:3001/tournaments.json");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		tournamentsFilename = "tournaments.json";
	}
	
	/**
	 * 
	 * @return The URI where tournaments are stored on the server
	 */
	public URI getURI()
	{
		return uri;
	}
	
	/**
	 * 
	 * @return The filename where tournaments are stored internally
	 */
	public String getTournamentsFilename()
	{
		return tournamentsFilename;
	}
	
	
}