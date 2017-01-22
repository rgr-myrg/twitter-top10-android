package net.usrlib.twittersearch.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class SearchResultItem {
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

	public static final List<SearchResultItem> fromTwitterSearchResponse(final TwitterSearchResponse response) {
		final List<SearchResultItem> results = new ArrayList<>();
		final List<TwitterStatusData> list = response.getStatuses();

		for (TwitterStatusData data : list) {

		}

		return results;
	}
}
