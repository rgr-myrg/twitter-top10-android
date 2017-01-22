package net.usrlib.twittersearch.sql;

import net.usrlib.twittersearch.model.SearchTermItem;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class SearchTermTable {
	public static final String TABLE_NAME = "SearchItems";
	public static final String TIMESTAMP  = "timestamp";

	public static final String CREATE_TABLE = String.format(
			"CREATE TABLE IF NOT EXISTS %s ("
					+ "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "%s TEXT NOT NULL,"
					+ "%s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
			TABLE_NAME,
			SearchTermItem.ITEM_ID_COLUMN,
			SearchTermItem.DESCRIPTION_COLUMN,
			TIMESTAMP
	);

	public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);

	public static final String SELECT_ALL = String.format(
			"SELECT * FROM %s ORDER BY %s DESC",
			TABLE_NAME, SearchTermItem.ITEM_ID_COLUMN
	);
}
