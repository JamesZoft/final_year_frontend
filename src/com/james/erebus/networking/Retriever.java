package com.james.erebus.networking;

import android.os.Build;


public abstract class Retriever {

	/**
	 * @param args
	 */

	protected final String baseUrl = "http://teamfrag.net:3001";

	public abstract void updatePage(); 

	public abstract void getByPast();

	public abstract void getByFuture();
	
	public abstract void getByOngoing();

	protected void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}
}
