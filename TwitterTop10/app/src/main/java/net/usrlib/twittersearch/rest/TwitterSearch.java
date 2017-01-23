package net.usrlib.twittersearch.rest;

import android.util.Base64;
import android.util.Log;

import net.usrlib.twittersearch.BuildConfig;
import net.usrlib.twittersearch.model.TwitterAuthResponse;
import net.usrlib.twittersearch.model.TwitterSearchResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterSearch {
	public static final String TAG = TwitterSearch.class.getSimpleName();
	public static final String BASE_URL = "https://api.twitter.com/";
	public static final String UTF8 = "UTF-8";

	private static TwitterSearch sInstance;

	private RetrofitApi mRetrofitApi;
	private String mApiAuthString;
	private String mSearchText;
	private String mSearchType = TwitterSearchResponse.SEARCH_TYPE_RECENT;
	private TwitterAuthResponse mTwitterAuthResponse;

	// Callbacks
	private OnAuthResponse mOnAuthResponse;
	private OnSearchResponse mOnSearchResponse;
	private OnError mOnError;

	public static final TwitterSearch getInstance() {
		if (sInstance == null) {
			sInstance = new TwitterSearch();
		}

		return sInstance;
	}

	public TwitterSearch onAuthResponse(final OnAuthResponse onAuthResponse) {
		mOnAuthResponse = onAuthResponse;
		return this;
	}

	public TwitterSearch onSearchResponse(final OnSearchResponse onSearchResponse) {
		mOnSearchResponse = onSearchResponse;
		return this;
	}

	public TwitterSearch onError(final OnError onError) {
		mOnError = onError;
		return this;
	}

	public TwitterSearch searchByHashTag(final String hashtag) {
		mSearchText = hashtag;
		return this;
	}

	public TwitterSearch searchType(final String searchType) {
		mSearchType = searchType;
		return this;
	}

	public void post() {
		postAuthRequest();
	}

	private void postAuthRequest() {
		// Check if request is already authorized
		if (mTwitterAuthResponse != null) {
			mOnAuthResponse.callback(mTwitterAuthResponse);
			postSearchRequest();
			return;
		}

		final Call<TwitterAuthResponse> oAuthCall = mRetrofitApi.requestAuthorization(
				String.format("Basic %s", mApiAuthString),
				TwitterAuthResponse.GRANT_TYPE_VALUE
		);

		oAuthCall.enqueue(new Callback<TwitterAuthResponse>() {
			@Override
			public void onResponse(Call<TwitterAuthResponse> call, Response<TwitterAuthResponse> response) {
				if (response == null || response.body() == null) {
					sendErrorMessage("PostAuthResponse returns NULL.");
					return;
				}

				mTwitterAuthResponse = response.body();
				mOnAuthResponse.callback(mTwitterAuthResponse);

				postSearchRequest();
			}

			@Override
			public void onFailure(Call<TwitterAuthResponse> call, Throwable t) {
				sendErrorMessage("PostAuthResponse failure.");
			}
		});
	}

	private void postSearchRequest() {
		if (mSearchText == null || mTwitterAuthResponse == null) {
			sendErrorMessage("Search Request is NULL.");
			return;
		}

		final Call<TwitterSearchResponse> searchCall = mRetrofitApi.requestTextSearch(
				String.format("Bearer %s", mTwitterAuthResponse.getAccessToken()),
				mSearchText,
				mSearchType
		);

		searchCall.enqueue(new Callback<TwitterSearchResponse>() {
			@Override
			public void onResponse(Call<TwitterSearchResponse> call, Response<TwitterSearchResponse> response) {
				if (response == null || response.body() == null) {
					sendErrorMessage("PostSearchResponse returns NULL.");
					return;
				}

				mOnSearchResponse.callback(response.body());
			}

			@Override
			public void onFailure(Call<TwitterSearchResponse> call, Throwable t) {
				sendErrorMessage("PostSearchResponse failure.");
			}
		});
	}

	private TwitterSearch() {
		this.mRetrofitApi = RetrofitClient.getClientWithUrl(BASE_URL).create(RetrofitApi.class);
		this.mApiAuthString = getApiAuthString();
	}

	/*
	 * Encode api key and secret.
	 * https://dev.twitter.com/oauth/application-only
	 */
	private String getApiAuthString() {
		String authString = null;

		try {
			authString = Base64.encodeToString(
					String.format("%s:%s",
							URLEncoder.encode(BuildConfig.TwitterApiKey, UTF8),
							URLEncoder.encode(BuildConfig.TwitterApiSecret, UTF8)
					).getBytes(),
					Base64.NO_WRAP
			);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return authString;
	}

	private void sendErrorMessage(final String message) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, message);
		}

		if (mOnError != null) {
			mOnError.callback("Error: " + message);
		}
	}

	// Callback interfaces
	public interface OnAuthResponse {
		void callback(TwitterAuthResponse response);
	}

	public interface OnSearchResponse {
		void callback(TwitterSearchResponse response);
	}

	public interface OnError {
		void callback(String msg);
	}
}
