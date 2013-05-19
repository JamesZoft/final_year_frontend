package com.james.erebus.networking;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.TimerTask;

import android.content.Context;

import com.james.erebus.core.Match;

public class GetMatchesTask extends TimerTask{
	
	private static Context taskContext;

	@Override
	public void run() {
		MatchRetriever m = new MatchRetriever();
		try {
			m.forceRetrieveFromServer(m.getURI(), m.getMatchesFilename());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MatchSubscriptionManager msm = new MatchSubscriptionManager();
		//NotificationManager nm = new NotificationManager();
		ArrayList<Match> newMatches = msm.compareSubbedMatches(taskContext);
		if(newMatches != null && !newMatches.isEmpty())
		{
			NotificationManager.setChangedMatches(newMatches);
		}
	}
	
	public static void setContext(Context c)
	{
		GetMatchesTask.taskContext = c;
	}

}
