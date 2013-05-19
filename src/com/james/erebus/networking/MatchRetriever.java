package com.james.erebus.networking;


import java.net.URI;
import java.net.URISyntaxException;

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
	
	public String getMatchesFilename()
	{
		return matchesFilename;
	}
	
	public URI getURI()
	{
		return uri;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//MatchRetriever m = new MatchRetriever();
		//m.retrieve(m.getURI(), m.);

	}
	
	

	public void getByPlayer()
	{
		
	}

	@Override
	public void getByOngoing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByPast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getByFuture() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePage() {
		// TODO Auto-generated method stub
		
	}

}