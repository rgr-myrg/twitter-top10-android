package net.usrlib.twittersearch.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import net.usrlib.twittersearch.BuildConfig;
import net.usrlib.twittersearch.sql.SearchResultTable;
import net.usrlib.twittersearch.sql.SearchTermTable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class DbHelper extends SQLiteOpenHelper {
	public static final String TAG = DbHelper.class.getSimpleName();
	public static final String DB_NAME = "net.usrlib.twittersearch.data";
	public static final int DB_VERSION = 1;
	public static final int NO_ID = -1;

	private static DbHelper sInstance = null;

	private DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (BuildConfig.DEBUG) Log.i(TAG, "onCreate starts up");

		db.execSQL(SearchTermTable.CREATE_TABLE);
		db.execSQL(SearchResultTable.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SearchTermTable.DROP_TABLE);
		db.execSQL(SearchResultTable.DROP_TABLE);
		onCreate(db);
	}

	public long insert(@NonNull final String tableName, @NonNull final ContentValues item) {
		final SQLiteDatabase db = getWritableDatabase();
		long newRecordId = -1;

		db.beginTransaction();

		try {
			//newRecordId = db.insertOrThrow(tableName, null, item);
			newRecordId = db.insertWithOnConflict(tableName, null, item, CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return newRecordId;
	}

	public int update(
			@NonNull final String tableName,
			@NonNull final ContentValues item,
			@NonNull final String where,
			@NonNull final String[] whereArgs) {

		final SQLiteDatabase db = getWritableDatabase();
		int numOfRows = NO_ID;

		db.beginTransaction();

		try {
			numOfRows = db.update(tableName, item, where, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return numOfRows;
	}

	public int delete(
			@NonNull final String tableName,
			@NonNull final String where,
			@NonNull final String[] whereArgs) {

		final SQLiteDatabase db = getWritableDatabase();
		int numOfRows = NO_ID;

		db.beginTransaction();

		try {
			numOfRows = db.delete(tableName, where, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return numOfRows;
	}

	public Cursor getDbCursorWithSql(final String sql) {
		if (BuildConfig.DEBUG) Debug.i(TAG, sql);

		final SQLiteDatabase db = getWritableDatabase();

		if (db == null) {
			return null;
		}

		final Cursor cursor = db.rawQuery(
				sql,
				null
		);

		return cursor;
	}

	public int bulkInsertWithOnConflict(final String tableName, final ContentValues[] values) {
		int rowsInserted = 0;
		final SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			for (ContentValues item : values) {
				if (BuildConfig.DEBUG) {
					Log.i(TAG, item.toString());
				}

				db.insertWithOnConflict(tableName, null, item, CONFLICT_REPLACE);
				rowsInserted++;
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return rowsInserted;
	}

	public static synchronized DbHelper getInstance(@NonNull final Context context) {
		// Use application context, to prevent accidentally leaking an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null && context != null) {
			sInstance = new DbHelper(context.getApplicationContext());
		}

		return sInstance;
	}
}
