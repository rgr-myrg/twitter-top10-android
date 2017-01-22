package net.usrlib.twittersearch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterStatusData {
	@SerializedName("id_str")
	private String idString;

	@SerializedName("text")
	private String text;

	@SerializedName("retweet_count")
	private int retweetCount;

	@SerializedName("favorite_count")
	private int favoriteCount;

	@SerializedName("user")
	private TwitterUserData user;

	@SerializedName("entities")
	private TwitterEntitiesData entities;

	public String getIdString() {
		return idString;
	}

	public String getText() {
		return text;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public TwitterUserData getUser() {
		return user;
	}

	public TwitterEntitiesData getEntities() {
		return entities;
	}
}
