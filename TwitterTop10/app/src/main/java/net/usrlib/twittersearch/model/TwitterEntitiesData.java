package net.usrlib.twittersearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterEntitiesData {
	@SerializedName("media")
	private List<Media> media;

	public List<Media> getMedia() {
		return media;
	}

	public class Media {
		@SerializedName("media_url")
		private String mediaUrl;

		@SerializedName("type")
		private String type;

		public String getMediaUrl() {
			return mediaUrl;
		}

		public String getType() {
			return type;
		}
	}
}
