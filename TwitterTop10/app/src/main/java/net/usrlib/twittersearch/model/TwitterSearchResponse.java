package net.usrlib.twittersearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterSearchResponse {
	public static final String RESULT_TYPE = "popular";

	@SerializedName("statuses")
	private List<TwitterStatusData> statuses;

	public List<TwitterStatusData> getStatuses() {
		return statuses;
	}
}
