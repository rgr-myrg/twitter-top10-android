package net.usrlib.twittersearch.rest;

import net.usrlib.twittersearch.model.TwitterAuthResponse;
import net.usrlib.twittersearch.model.TwitterSearchResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public interface RetrofitApi {
	/*
	 * https://dev.twitter.com/oauth/application-only
	 */
	@FormUrlEncoded
	@POST("oauth2/token")
	Call<TwitterAuthResponse> requestAuthorization(
			@Header("Authorization") String authorization,
			@Field("grant_type") String grantType
	);

	/*
	 * https://dev.twitter.com/rest/reference/get/search/tweets
	 */
	@GET("1.1/search/tweets.json")
	Call<TwitterSearchResponse> requestTextSearch(
			@Header("Authorization") String authorization,
			@Query("q") String textQuery,
			@Query("result_type") String resultType
	);
}
