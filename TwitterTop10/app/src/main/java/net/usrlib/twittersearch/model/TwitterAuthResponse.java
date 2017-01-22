package net.usrlib.twittersearch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model for Twitter OAuth Response.
 *
 * Ex response: {"token_type":"bearer","access_token":"AAAA%2FAAA%3DAAAAAAAA"}
 * https://dev.twitter.com/oauth/reference/post/oauth2/token
 *
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterAuthResponse {
	public static final String GRANT_TYPE_VALUE = "client_credentials";

	@SerializedName("token_type")
	private String tokenType;

	@SerializedName("access_token")
	private String accessToken;

	public String getTokenType() {
		return tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}
}
