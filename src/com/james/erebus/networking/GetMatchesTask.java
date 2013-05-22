package com.james.erebus.networking;

import java.net.UnknownHostException;
import java.util.TimerTask;

/**
 * A {@link java.util.TimerTask} for getting match information from the server
 * @author james
 *
 */

public class GetMatchesTask extends TimerTask{
	

	@Override
	public void run() {
		MatchRetriever m = new MatchRetriever();
		try {
			m.forceRetrieveFromServer(m.getURI(), m.getMatchesFilename());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
