package net.usrlib.twittersearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterSearchResponse {
	public static final String SEARCH_TYPE_POPULAR = "popular";
	public static final String SEARCH_TYPE_RECENT  = "recent";
	public static final String SEARCH_TYPE_MIXED   = "mixed";

	public static final int FREQUENCY_HIGH = 1;
	public static final int FREQUENCY_MEDIUM = 3;
	public static final int FREQUENCY_LOW = 5;

	public static final int SEARCH_COUNT_HIGH = 100;
	public static final int SEARCH_COUNT_MEDIUM = 50;
	public static final int SEARCH_COUNT_LOW = 25;

	//Ex: https://twitter.com/Thai_Buddhism/status/824071408804171776
	public static final String TWITTER_STATUSES_URL = "https://twitter.com/?/status/?";

	@SerializedName("statuses")
	private List<TwitterStatusData> statuses;

	public List<TwitterStatusData> getStatuses() {
		return statuses;
	}
}
