package com.james.erebus.networking;

import java.net.URI;
import java.net.URISyntaxException;

public class SubscriptionRetriever extends Retriever{

	URI uri;
	String filename;
	
	public SubscriptionRetriever()
	{
		try {
			uri = new URI("http://teamfrag.net:3002/subscriptions.json");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filename = "subscriptions.json";
	}
	
	public SubscriptionRetriever(String subId)
	{
		try {
			uri = new URI("http://teamfrag.net:3002/subscriptions/" + subId + ".json");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getSubscriptionsFilename()
	{
		return filename;
	}
	
	public URI getURI()
	{
		return uri;
	}
	
	@Override
	public void updatePage() {
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
	public void getByOngoing() {
		// TODO Auto-generated method stub
		
	}

}
