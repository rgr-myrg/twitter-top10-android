package net.usrlib.twittersearch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import net.usrlib.twittersearch.model.TwitterSearchResponse;

/**
 * Created by rgr-myrg on 1/23/17.
 */

public class Preferences {
	public static final String SEARCH_TYPE_KEY  = "searchType";
	public static final String SEARCH_COUNT_KEY = "searchCount";
	public static final String FREQUENCY_KEY = "frequency";

	public static final void setRefreshFrequency(final Context context, final int minutes) {
		setInt(context, FREQUENCY_KEY, minutes);
	}

	public static final int getRefreshFrequency(final Context context) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(FREQUENCY_KEY, TwitterSearchResponse.FREQUENCY_HIGH);
	}

	public static final void setSearchCount(final Context context, final int count) {
		setInt(context, SEARCH_COUNT_KEY, count);
	}

	public static final int getSearchCount(final Context context) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(SEARCH_COUNT_KEY, TwitterSearchResponse.SEARCH_COUNT_LOW);
	}

	public static final void setSearchType(final Context context, final String searchType) {
		setString(context, SEARCH_TYPE_KEY, searchType);
	}

	public static final String getSearchType(final Context context) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(SEARCH_TYPE_KEY, TwitterSearchResponse.SEARCH_TYPE_RECENT);
	}

	public static final void setString(final Context context,
										final String key,
										final String value) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final SharedPreferences.Editor editor = preferences.edit();

		editor.putString(key, value);
		editor.commit();
	}

	public static final void setInt(final Context context,
									final String key,
									final int value) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final SharedPreferences.Editor editor = preferences.edit();
		Log.i("PREF", key + ":" + value);
		editor.putInt(key, value);
		editor.commit();
	}
}