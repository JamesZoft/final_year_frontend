package com.james.erebus.networking;

import java.net.UnknownHostException;
import java.util.TimerTask;

/**
 * A {@link java.util.TimerTask} for getting tournament information from the server
 * @author james
 *
 */

public class GetTournamentsTask extends TimerTask{

	@Override
	public void run() {
		TournamentRetriever t = new TournamentRetriever();
		try {
			t.forceRetrieveFromServer(t.getURI(), t.getTournamentsFilename());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
