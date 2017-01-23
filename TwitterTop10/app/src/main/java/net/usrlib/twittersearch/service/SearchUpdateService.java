package net.usrlib.twittersearch.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import net.usrlib.twittersearch.BuildConfig;
import net.usrlib.twittersearch.model.SearchTermItem;
import net.usrlib.twittersearch.presenter.Presenter;
import net.usrlib.twittersearch.util.DbHelper;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class SearchUpdateService extends IntentService {
	public static final String TAG = SearchUpdateService.class.getSimpleName();
	public static final String STATE_CHANGE = SearchUpdateService.class.getCanonicalName();
	public static final String STATUS_UPDATE = "statusUpdate";

	public static final int STATUS_NOT_STARTED = 100;
	public static final int STATUS_STARTED  = 101;
	public static final int STATUS_COMPLETE = 102;

	public SearchUpdateService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final int itemId = intent.getIntExtra(SearchTermItem.ITEM_ID_COLUMN, DbHelper.NO_ID);
		final String query = intent.getStringExtra(SearchTermItem.DESCRIPTION_COLUMN);

		if (BuildConfig.DEBUG) {
			Log.i(TAG, "onHandleIntent " + itemId + ":" + query);
		}

		sendBroadcast(
				new Intent(STATE_CHANGE)
						.putExtra(STATUS_UPDATE, STATUS_STARTED)
		);

		Presenter.startSearchRequest(getApplicationContext(), itemId, query, response -> {
			final int resultType = response.getType();

			if (resultType == Presenter.SearchResult.SUCCESS) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, response.getSearchId() + ":" + response.getSearchTerm());
				}

				sendBroadcast(
						new Intent(STATE_CHANGE)
								.putExtra(STATUS_UPDATE, STATUS_COMPLETE)
				);
			}
		});
	}
}
