package net.usrlib.twittersearch.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.usrlib.twittersearch.BuildConfig;
import net.usrlib.twittersearch.model.SearchResultItem;
import net.usrlib.twittersearch.model.SearchTermItem;
import net.usrlib.twittersearch.model.TwitterStatusData;
import net.usrlib.twittersearch.rest.TwitterSearch;
import net.usrlib.twittersearch.sql.SearchResultTable;
import net.usrlib.twittersearch.sql.SearchTermTable;
import net.usrlib.twittersearch.util.DbHelper;
import net.usrlib.twittersearch.util.Debug;
import net.usrlib.twittersearch.util.Preferences;

import java.util.List;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class Presenter {
	public static final String TAG = Presenter.class.getSimpleName();

	public static final int AUTH_RESPONSE_ERROR = 100;
	public static final int EMPTY_SEARCH_TERM = 101;
	public static final int NO_RESULTS_FOUND = 102;
	public static final int SEARCH_NOT_ADDED = 103;
	public static final int SEARCH_ERROR = 104;
	public static final int SEARCH_RESULTS_READY = 105;
	public static final int SEARCH_RESULTS_INCOMPLETE = 106;

	public static final void startSearchRequest(
			final Context context,
			final int itemId,
			final String hashTag,
			final OnNewSearchReady callback) {

		if (hashTag == null || hashTag.isEmpty()) {
			callback.run(new SearchResult(SearchResult.EXCEPTION, EMPTY_SEARCH_TERM));
			return;
		}

		final String searchTerm = SearchTermItem.formatDescriptionValue(hashTag);

		TwitterSearch.getInstance()
				.onAuthResponse(response -> {
					if (response == null) {
						callback.run(new SearchResult(SearchResult.EXCEPTION, AUTH_RESPONSE_ERROR));
					}
				})
				.onSearchResponse(response -> {
					List<TwitterStatusData> results = response.getStatuses();

					if (results == null || results.isEmpty()) {
						callback.run(new SearchResult(SearchResult.EXCEPTION, NO_RESULTS_FOUND));
						return;
					}

					final int newRowId = addSearchTermToDb(context, itemId, searchTerm);

					if (newRowId == DbHelper.NO_ID) {
						callback.run(new SearchResult(SearchResult.EXCEPTION, SEARCH_NOT_ADDED));
						return;
					}

					final int rows = addNewSearchResults(context, newRowId, results);

					if (rows == DbHelper.NO_ID) {
						callback.run(new SearchResult(SearchResult.EXCEPTION, SEARCH_RESULTS_INCOMPLETE));
						return;
					}

					final SearchResult result = new SearchResult(
							SearchResult.SUCCESS, SEARCH_RESULTS_READY
					);

					result.setSearchId(newRowId);
					result.setSearchTerm(searchTerm);

					callback.run(result);
				})
				.onError(msg -> {
					final SearchResult result = new SearchResult(SearchResult.EXCEPTION, SEARCH_ERROR);
					result.setMessage(msg);
					callback.run(result);
				})
				.searchWithHashTag(searchTerm)
				.searchType(Preferences.getSearchType(context))
				.searchCount(Preferences.getSearchCount(context))
				.post();
	}

	public static final int addSearchTermToDb(
			final Context context,
			final int itemId,
			final String description) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(
				SearchTermItem.DESCRIPTION_COLUMN,
				SearchTermItem.formatDescriptionValue(description)
		);

		// Carry over itemId if we have one
		if (itemId != DbHelper.NO_ID) {
			contentValues.put(
					SearchTermItem.ITEM_ID_COLUMN,
					itemId
			);
		}

		final int rowId = (int) DbHelper.getInstance(context).insert(
				SearchTermTable.TABLE_NAME,
				contentValues
		);

		// Always set the position to the new row id.
		// This allows for updating the position
		// when the user changes the order on the UI.
		if (rowId > 0) {
			final ContentValues values = new ContentValues();
			values.put(SearchTermItem.POSITION_COLUMN, rowId);

			DbHelper.getInstance(context).update(
					SearchTermTable.TABLE_NAME,
					values,
					SearchTermTable.WHERE_ITEM_ID,
					new String[]{String.valueOf(rowId)}
			);
		}

		return rowId;
	}

	public static final int addNewSearchResults(
			final Context context,
			final int rowId,
			final List<TwitterStatusData> results) {

		final int rows = DbHelper.getInstance(context).bulkInsertWithOnConflict(
				SearchResultTable.TABLE_NAME,
				SearchResultItem.toContentValuesWithSearchTermId(results, rowId)
		);

		return rows;
	}

	public static final Cursor getSearchItemsFromDb(final Context context) {
		return DbHelper
				.getInstance(context)
				.getDbCursorWithSql(
						SearchTermTable.SELECT_ALL
				);
	}

	public static final Cursor getResultItemsFromDb(final Context context, final int itemId) {
		final int searchCount = Preferences.getSearchCount(context);
		return DbHelper
				.getInstance(context)
				.getDbCursorWithSql(
						SearchResultTable.SELECT_ALL_WITH_ITEM_ID
								.replaceFirst("\\?", String.valueOf(itemId))
								.replaceFirst("\\?", String.valueOf(searchCount))
				);
	}

	public static final Cursor deleteSearchItemFromDb(final Context context, final int itemId) {
		final DbHelper dbHelper = DbHelper.getInstance(context);
		final String[] stringId = new String[]{String.valueOf(itemId)};
		final int rows = dbHelper.delete(
				SearchTermTable.TABLE_NAME,
				SearchTermTable.WHERE_ITEM_ID,
				stringId
		);

		if (rows > 0) {
			dbHelper.delete(
					SearchResultTable.TABLE_NAME,
					SearchResultTable.WHERE_ITEM_ID,
					stringId
			);
		}

		return getSearchItemsFromDb(context);
	}

	public static final Cursor swapSearchItemsInDb(
			final Context context,
			final int intemId,
			final int targetId) {
		// TODO: Items in between must also be reordered.
		// Set target to temp value
		swapItemIds(context, targetId, DbHelper.NO_ID);

		// Direction from -> to
		swapItemIds(context, intemId, targetId);

		// Direction temp value -> from
		swapItemIds(context, DbHelper.NO_ID, intemId);

		return getSearchItemsFromDb(context);
	}

	public static final int swapItemIds(final Context context, final int itemId, final int targetId) {
		final ContentValues values = new ContentValues();
		values.put(SearchTermItem.POSITION_COLUMN, targetId);

		if (BuildConfig.DEBUG) {
			Debug.i(TAG, "swapItemIds: " + itemId + "->" + targetId);
		}

		return DbHelper.getInstance(context).update(
				SearchTermTable.TABLE_NAME,
				values,
				SearchTermTable.WHERE_ITEM_ID,
				new String[]{String.valueOf(itemId)}
		);
	}

	public interface OnNewSearchReady {
		void run(SearchResult response);
	}

	public static class SearchResult {
		public static final int EXCEPTION = -1;
		public static final int SUCCESS = 1;

		private int type;
		private int searchId;
		private int status;

		private String message = "";
		private String searchTerm = "";

		public SearchResult(int type, int status) {
			this.type = type;
			this.status = status;
		}

		public int getType() {
			return type;
		}

		public int getSearchId() {
			return searchId;
		}

		public int getStatus() {
			return status;
		}

		public String getMessage() {
			return message;
		}

		public String getSearchTerm() {
			return searchTerm;
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setSearchId(int searchId) {
			this.searchId = searchId;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setSearchTerm(String searchTerm) {
			this.searchTerm = searchTerm;
		}
	}
}
