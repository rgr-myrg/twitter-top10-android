package net.usrlib.twittersearch.sql;

import net.usrlib.twittersearch.model.SearchTermItem;

import java.util.Locale;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class SearchTermTable {
	public static final String TABLE_NAME = "SearchItems";
	public static final String TIMESTAMP  = "timestamp";

	public static final String CREATE_TABLE = String.format(
			Locale.getDefault(),
			"CREATE TABLE IF NOT EXISTS %s ("
					+ "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "%s INTEGER,"
					+ "%s TEXT NOT NULL,"
					+ "%s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
			TABLE_NAME,
			SearchTermItem.ITEM_ID_COLUMN,
			SearchTermItem.POSITION_COLUMN,
			SearchTermItem.DESCRIPTION_COLUMN,
			TIMESTAMP
	);

	public static final String DROP_TABLE = String.format(
			Locale.getDefault(),
			"DROP TABLE IF EXISTS %s",
			TABLE_NAME
	);

	public static final String SELECT_ALL = String.format(
			Locale.getDefault(),
			"SELECT * FROM %s ORDER BY %s DESC",
			TABLE_NAME, SearchTermItem.POSITION_COLUMN
	);

	public static final String WHERE_ITEM_ID = String.format(
			Locale.getDefault(),
			"%s = ?",
			SearchTermItem.ITEM_ID_COLUMN
	);
}
