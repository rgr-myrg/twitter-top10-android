package net.usrlib.twittersearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/21/17.
 */

public class TwitterEntitiesData {
	@SerializedName("media")
	private List<Media> media;

	@SerializedName("urls")
	private List<EntityUrl> urls;

	public List<Media> getMedia() {
		return media;
	}

	public List<EntityUrl> getUrls() {
		return urls;
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

	public class EntityUrl {
		@SerializedName("url")
		private String url;

		@SerializedName("expanded_url")
		private String expandedUrl;

		@SerializedName("display_url")
		private String displayUrl;

		public String getUrl() {
			return url;
		}

		public String getExpandedUrl() {
			return expandedUrl;
		}

		public String getDisplayUrl() {
			return displayUrl;
		}
	}
}
