package com.james.erebus.networking;

import android.os.Build;


public abstract class Retriever {

	/**
	 * @param args
	 */

	protected final String baseUrl = "http://192.168.0.11:3000";

	public abstract void updatePage(); 

	public abstract void getBySubbed();

	public abstract void getByUnsubbed();

	public abstract void getByOngoing();

	public abstract void getByPast();

	public abstract void getByFuture();

	public abstract void getEntryByName();

	protected void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}
