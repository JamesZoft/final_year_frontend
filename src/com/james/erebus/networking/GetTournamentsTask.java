package com.james.erebus.networking;

import java.util.ArrayList;
import java.util.TimerTask;

import android.content.Context;

import com.james.erebus.core.Tournament;

public class GetTournamentsTask extends TimerTask{

	private static Context taskContext;

	@Override
	public void run() {
		TournamentRetriever t = new TournamentRetriever();
		t.forceRetrieveFromServer(t.getURI(), t.getTournamentsFilename());
		TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
		ArrayList<Tournament> newTournaments = tsm.compareSubbedTournaments(taskContext);
		if(newTournaments != null && !newTournaments.isEmpty())
		{
			NotificationManager.setChangedTournaments(newTournaments);
		}
	}
	
	public static void setContext(Context c)
	{
		GetTournamentsTask.taskContext = c;
	}

}
