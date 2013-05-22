package com.james.erebus.networking;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Child class of the {@link com.james.erebus.networking.Retriever} class for retrieving subscriptions
 * @author james
 *
 */

public class SubscriptionRetriever extends Retriever{

	URI uri;
	String filename;
	
	public SubscriptionRetriever()
	{
		try {
			uri = new URI("http://teamfrag.net:3002/subscriptions.json");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		filename = "subscriptions.json";
	}
	
	public SubscriptionRetriever(String subId)
	{
		try {
			uri = new URI("http://teamfrag.net:3002/subscriptions/" + subId + ".json");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The filename where subscriptions are stored internally
	 */
	public String getSubscriptionsFilename()
	{
		return filename;
	}
	
	/**
	 * 
	 * @return The URI where subscriptions are stored on the server
	 */
	public URI getURI()
	{
		return uri;
	}
}
