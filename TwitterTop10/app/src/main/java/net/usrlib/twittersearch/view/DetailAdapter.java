package net.usrlib.twittersearch.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.material.MaterialTheme;
import net.usrlib.material.Theme;
import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.SearchResultItem;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class DetailAdapter extends RecyclerView.Adapter {
	protected final MaterialTheme mMaterialTheme = MaterialTheme.get(Theme.TOPAZ);
	protected LayoutInflater mInflater = null;
	protected Context mContext = null;
	protected Cursor mCursor = null;
	protected OnItemClick mOnItemClick = null;

	public DetailAdapter(Context context, Cursor cursor, OnItemClick callback) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext  = context;
		this.mCursor   = cursor;
		this.mOnItemClick = callback;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(
				mInflater.inflate(
						R.layout.detail_card_view,
						parent,
						false
				)
		);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		final ViewHolder viewHolder = (ViewHolder) holder;
		final SearchResultItem data = getItem(position);

		if (data != null && position >= 0 && position < mCursor.getCount()) {
			viewHolder.bindData(data);
		}
	}

	@Override
	public int getItemCount() {
		return mCursor == null ? 0 : mCursor.getCount();
	}

	public SearchResultItem getItem(final int position) {
		mCursor.moveToPosition(position);

		return SearchResultItem.fromDbCursor(mCursor);
	}

	public void changeCursor(Cursor cursor) {
		Cursor swappedCursor = swapCursor(cursor);

		if (swappedCursor != null) {
			swappedCursor.close();
		}
	}

	public Cursor swapCursor(Cursor cursor) {
		if (mCursor == cursor) {
			return null;
		}

		final Cursor previousCursor = mCursor;

		mCursor = cursor;

		if (cursor != null) {
			this.notifyDataSetChanged();
		}

		return previousCursor;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView profileImage;
		public ImageView tweetImage;
		public TextView userName;
		public TextView tweetText;
		public TextView tweetStats;
		public TextView followers;

		public ViewHolder(View view) {
			super(view);

			profileImage = (ImageView) view.findViewById(R.id.user_profile_image);
			tweetImage = (ImageView) view.findViewById(R.id.tweet_image);
			userName   = (TextView) view.findViewById(R.id.user_name);
			tweetText  = (TextView) view.findViewById(R.id.tweet_text);
			tweetStats = (TextView) view.findViewById(R.id.tweet_stats);
			followers  = (TextView) view.findViewById(R.id.followers_count);
		}

		public void bindData(final SearchResultItem data) {
			Glide.with(mContext).load(data.getUserProfileImageUrl()).centerCrop().into(profileImage);

			userName.setText(data.getUserName());
			tweetText.setText(data.getTweetText());
			tweetStats.setText(
					String.format("%s %d",
							mContext.getString(R.string.heart),
							data.getTweetFavoriteCount())
			);
			followers.setText(
					String.format("%s %d",
							mContext.getString(R.string.followers_label),
							data.getUserFollowersCount())
			);

			String mediaType = data.getTweetMediaType();
			if (mediaType != null && mediaType.equalsIgnoreCase("photo")) {
				String imageUrl = data.getTweetMediaUrl();

				if (imageUrl != null) {
					Glide.with(mContext).load(imageUrl).centerCrop().into(tweetImage);
				} else {
					tweetImage.setVisibility(View.GONE);
				}
			} else {
				tweetImage.setVisibility(View.GONE);
			}
		}
	}

	public interface OnItemClick {
		void run(int position);
	}
}
