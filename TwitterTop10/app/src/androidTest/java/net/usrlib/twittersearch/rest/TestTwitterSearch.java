package net.usrlib.twittersearch.rest;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import net.usrlib.twittersearch.model.TwitterAuthResponse;
import net.usrlib.twittersearch.model.TwitterSearchResponse;
import net.usrlib.twittersearch.model.TwitterStatusData;
import net.usrlib.twittersearch.model.TwitterUserData;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rgr-myrg on 1/20/17.
 */

@RunWith(AndroidJUnit4.class)
public class TestTwitterSearch {
	private TwitterAuthResponse mTwitterAuthResponse = null;
	private TwitterSearchResponse mTwitterSearchResponse = null;

	@Test
	public void testAuthResponse() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);

		TwitterSearch.getInstance().onAuthResponse(response -> {
			Log.i("TestTwitterSearch", "onAuthResponse " + response.getTokenType());
			mTwitterAuthResponse = response;
			signal.countDown();
		}).post();

		signal.await(5, TimeUnit.SECONDS);

		assertNotNull(
				"onAuthResponse should return the auth response.",
				mTwitterAuthResponse
		);
		assertNotNull(
				"onAuthResponse should return a token",
				mTwitterAuthResponse.getAccessToken()
		);
		assertEquals(
				"onAuthResponse should return a valid token type",
				TwitterSearch.TOKEN_TYPE,
				mTwitterAuthResponse.getTokenType()
		);
	}

	@Test
	public void testSearchResponse() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);
		TwitterSearch.getInstance()
				.onAuthResponse(response -> {
				})
				.onSearchResponse(response -> {
					mTwitterSearchResponse = response;
					signal.countDown();
				})
				.searchWithHashTag("trump")
				.post();

		signal.await(5, TimeUnit.SECONDS);

		assertNotNull(
				"onSearchResponse should return a search response.",
				mTwitterSearchResponse
		);

		final List<TwitterStatusData> tweetData = mTwitterSearchResponse.getStatuses();

		assertNotNull(
				"getStatuses should return tweet data.",
				tweetData
		);
		assertEquals(
				"getStatuses should return tweet data items",
				!tweetData.isEmpty(),
				true
		);

		final TwitterStatusData statusData = tweetData.get(0);

		assertNotNull(
				"getIdString should return a tweet id",
				statusData.getIdString()
		);
		assertNotNull(
				"getText should return tweet content",
				statusData.getText()
		);

		final TwitterUserData userData = statusData.getUser();

		assertNotNull(
				"getUser should return the user object",
				userData
		);
		assertNotNull(
				"getId should return the user id",
				userData.getId()
		);
	}
}
