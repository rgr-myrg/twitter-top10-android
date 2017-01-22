package net.usrlib.twittersearch.sql;

import net.usrlib.twittersearch.model.SearchItem;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class SearchItemTable {
	public static final String TABLE_NAME = "SearchItems";
	public static final String TIMESTAMP  = "timestamp";

	public static final String CREATE_TABLE = String.format(
			"CREATE TABLE IF NOT EXISTS %s ("
					+ "%s TEXT NOT NULL,"
					+ "%s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
			TABLE_NAME,
			SearchItem.DESCRIPTION_COLUMN,
			TIMESTAMP
	);

	public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);

//	public static final String SELECT_ITEM_COUNT_WITH_BOOK_ID = String.format(
//			"SELECT %s FROM %s WHERE %s = ?", Book.ITEM_COUNT, TABLE_NAME, Book.BOOK_ID
//	);

	public static final String SELECT_ALL = String.format("SELECT * FROM %s", TABLE_NAME);
}
