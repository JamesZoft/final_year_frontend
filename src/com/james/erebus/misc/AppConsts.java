package com.james.erebus.misc;

import android.app.Activity;

/**
 * Helper class purely to hold a reference to the current {@link android.app.Activity} on the screen.
 * This is updated through the onResume() method of every activity, which has been overriden
 * @author james
 *
 */

public class AppConsts {
	
	public static Activity currentActivity = null;

}
