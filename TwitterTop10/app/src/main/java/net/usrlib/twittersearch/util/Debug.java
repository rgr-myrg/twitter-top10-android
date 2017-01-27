package net.usrlib.twittersearch.util;

import android.util.Log;

/**
 * Created by rgr-myrg on 1/26/17.
 */

public class Debug {
	public static final void i(String tag, String msg) {
		msg = String.format("[%s] %s", tag, msg);
		Log.i("@APP", msg);
	}
}