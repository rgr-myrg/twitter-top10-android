package net.usrlib.twittersearch.sql;

import net.usrlib.twittersearch.model.SearchResultItem;
import net.usrlib.twittersearch.model.SearchTermItem;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class SearchResultTable {
	public static final String TABLE_NAME = "SearchResult";
	public static final String TIMESTAMP = "timestamp";

	public static final String CREATE_TABLE = String.format(
			"CREATE TABLE IF NOT EXISTS %s ("
					+ "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s INTEGER,"
					+ "%s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
			TABLE_NAME,
			SearchResultItem.RESULT_ID_COLUMN,
			SearchTermItem.ITEM_ID_COLUMN,
			SearchResultItem.USER_ID_COLUMN,
			SearchResultItem.USERNAME_COLUMN,
			SearchResultItem.USER_SCREEN_NAME_COLUMN,
			SearchResultItem.PROFILE_IMAGE_URL_COLUMN,
			SearchResultItem.PROFILE_BACKGROUND_COLOR_COLUMN,
			SearchResultItem.ENTITY_URL_COLUMN,
			SearchResultItem.TWEET_ID_STRING_COLUMN,
			SearchResultItem.TWEET_TEXT_COLUMN,
			SearchResultItem.TWEET_MEDIA_URL_COLUMN,
			SearchResultItem.TWEET_MEDIA_TYPE_COLUMN,
			SearchResultItem.TWEET_FAVORITE_COUNT_COLUMN,
			SearchResultItem.RETWEET_COUNT_COLUMN,
			SearchResultItem.FOLLOWERS_COUNT_COLUMN,
			SearchResultItem.FRIENDS_COUNT_COLUMN,
			SearchResultItem.STATUSES_COUNT_COLUMN,
			TIMESTAMP
	);

	public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);

	public static final String SELECT_ALL = String.format("SELECT * FROM %s", TABLE_NAME);

	public static final String SELECT_ALL_WITH_ITEM_ID = String.format(
			"%s WHERE %s = ? ORDER BY %s DESC LIMIT ?",
			SELECT_ALL, SearchTermItem.ITEM_ID_COLUMN, TIMESTAMP
	);
}
