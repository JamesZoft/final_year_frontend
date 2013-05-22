package com.james.erebus.networking;


import java.net.URI;
import java.net.URISyntaxException;

/**
 * Child class of {@link com.james.erebus.networking.Retriever} that specifies the URI and filename for match-specific retrieval
 * @author james
 *
 */

public class MatchRetriever extends Retriever{

	URI uri;
	String matchesFilename;
	
	public MatchRetriever()
	{
		try {
			uri = new URI("http://teamfrag.net:3001/matches.json");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		matchesFilename = "matches.json";
	}
	
	/**
	 * 
	 * @return The filename that holds match information
	 */
	public String getMatchesFilename()
	{
		return matchesFilename;
	}
	
	/**
	 * 
	 * @return The {@link java.net.URI} that leads to the match information on the server
	 */
	public URI getURI()
	{
		return uri;
	}


}