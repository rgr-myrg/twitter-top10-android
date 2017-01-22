package net.usrlib.twittersearch.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class SearchResultItem {
	public static final String RESULT_ID_COLUMN = "resultId";
	public static final String USER_ID_COLUMN = "userId";
	public static final String USERNAME_COLUMN = "userName";
	public static final String PROFILE_IMAGE_URL_COLUMN = "userProfileImageUrl";
	public static final String PROFILE_BACKGROUND_COLOR_COLUMN = "userProfileBackgroundColor";
	public static final String TWEET_TEXT_COLUMN = "tweetText";
	public static final String TWEET_MEDIA_URL_COLUMN = "tweetMediaUrl";
	public static final String TWEET_MEDIA_TYPE_COLUMN = "tweetMediaType";
	public static final String TWEET_FAVORITE_COUNT_COLUMN = "tweetFavoriteCount";
	public static final String RETWEET_COUNT_COLUMN = "retweetCount";
	public static final String FOLLOWERS_COUNT_COLUMN = "userFollowersCount";
	public static final String FRIENDS_COUNT_COLUMN = "userFriendsCount";
	public static final String STATUSES_COUNT_COLUMN = "userStatusesCount";

	private int resultId;
	private String userId;
	private String userName;
	private String userProfileImageUrl;
	private String userProfileBackgroundColor;
	private String tweetText;
	private String tweetMediaUrl;
	private String tweetMediaType;

	private int tweetFavoriteCount;
	private int retweetCount;
	private int userFollowersCount;
	private int userFriendsCount;
	private int userStatusesCount;

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserProfileImageUrl() {
		return userProfileImageUrl;
	}

	public String getUserProfileBackgroundColor() {
		return userProfileBackgroundColor;
	}

	public int getUserFollowersCount() {
		return userFollowersCount;
	}

	public String getTweetText() {
		return tweetText;
	}

	public String getTweetMediaUrl() {
		return tweetMediaUrl;
	}

	public String getTweetMediaType() {
		return tweetMediaType;
	}

	public int getTweetFavoriteCount() {
		return tweetFavoriteCount;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public int getUserFriendsCount() {
		return userFriendsCount;
	}

	public int getUserStatusesCount() {
		return userStatusesCount;
	}

	public static final ContentValues[] toContentValuesWithSearchTermId(
			final List<TwitterStatusData> list, final int searchTermId) {

		final ContentValues[] contentValues = new ContentValues[list.size()];

		for (int i = 0; i < list.size(); i++) {
			TwitterStatusData data = list.get(i);

			ContentValues values = new ContentValues();
			values.put(SearchTermItem.ITEM_ID_COLUMN, searchTermId);
			values.put(USER_ID_COLUMN, data.getUser().getId());
			values.put(USERNAME_COLUMN, data.getUser().getName());
			values.put(PROFILE_IMAGE_URL_COLUMN, data.getUser().getProfileImageUrl());
			values.put(PROFILE_BACKGROUND_COLOR_COLUMN, data.getUser().getProfileBackgroundColor());
			values.put(TWEET_TEXT_COLUMN, data.getText());

			TwitterEntitiesData entities = data.getEntities();

			if (entities != null) {
				List<TwitterEntitiesData.Media> mediaList = entities.getMedia();
				if (mediaList != null && !mediaList.isEmpty() && mediaList.size() > 0) {
					values.put(TWEET_MEDIA_URL_COLUMN, mediaList.get(0).getMediaUrl());
					values.put(TWEET_MEDIA_TYPE_COLUMN, mediaList.get(0).getType());
				}
			}

			values.put(TWEET_FAVORITE_COUNT_COLUMN, data.getFavoriteCount());
			values.put(RETWEET_COUNT_COLUMN, data.getRetweetCount());
			values.put(FOLLOWERS_COUNT_COLUMN, data.getUser().getFollowersCount());
			values.put(FRIENDS_COUNT_COLUMN, data.getUser().getFriendsCount());
			values.put(STATUSES_COUNT_COLUMN, data.getUser().getStatusesCount());

			contentValues[i] = values;
		}

		return contentValues;
	}

	public static SearchResultItem fromDbCursor(final Cursor cursor) {
		if (cursor == null) {
			return null;
		}

		final SearchResultItem searchItem = new SearchResultItem();

		searchItem.userName = cursor.getString(cursor.getColumnIndex(USERNAME_COLUMN));
		searchItem.userProfileImageUrl = cursor.getString(cursor.getColumnIndex(PROFILE_IMAGE_URL_COLUMN));
		searchItem.userStatusesCount = cursor.getInt(cursor.getColumnIndex(STATUSES_COUNT_COLUMN));
		searchItem.userFollowersCount = cursor.getInt(cursor.getColumnIndex(FOLLOWERS_COUNT_COLUMN));
		searchItem.userFriendsCount = cursor.getInt(cursor.getColumnIndex(FRIENDS_COUNT_COLUMN));
		searchItem.tweetText = cursor.getString(cursor.getColumnIndex(TWEET_TEXT_COLUMN));
		searchItem.tweetMediaUrl = cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_URL_COLUMN));
		searchItem.tweetMediaType = cursor.getString(cursor.getColumnIndex(TWEET_MEDIA_TYPE_COLUMN));
		searchItem.tweetFavoriteCount = cursor.getInt(cursor.getColumnIndex(TWEET_FAVORITE_COUNT_COLUMN));

		return searchItem;
	}
}
