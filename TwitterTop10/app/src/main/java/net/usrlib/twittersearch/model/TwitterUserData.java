package net.usrlib.twittersearch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterUserData {
	@SerializedName("id")
	private String id;

	@SerializedName("name")
	private String name;

	@SerializedName("location")
	private String location;

	@SerializedName("profile_background_color")
	private String profileBackgroundColor;

	@SerializedName("profile_link_color")
	private String profileLinkColor;

	@SerializedName("profile_image_url")
	private String profileImageUrl;

	@SerializedName("followers_count")
	private String followersCount;

	@SerializedName("friends_count")
	private int friendsCount;

	@SerializedName("statuses_count")
	private int statusesCount;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getProfileBackgroundColor() {
		return profileBackgroundColor;
	}

	public String getProfileLinkColor() {
		return profileLinkColor;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getFollowersCount() {
		return followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public int getStatusesCount() {
		return statusesCount;
	}
}
