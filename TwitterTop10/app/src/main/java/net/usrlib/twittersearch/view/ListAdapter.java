package net.usrlib.twittersearch.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.usrlib.material.MaterialTheme;
import net.usrlib.material.Theme;
import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.SearchTermItem;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class ListAdapter extends RecyclerView.Adapter {
	protected final MaterialTheme mMaterialTheme = MaterialTheme.get(Theme.TOPAZ);
	protected LayoutInflater mInflater = null;
	protected Context mContext = null;
	protected Cursor mCursor = null;
	protected OnItemClick mOnItemClick = null;

	public ListAdapter(Context context, Cursor cursor, OnItemClick callback) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext  = context;
		this.mCursor   = cursor;
		this.mOnItemClick = callback;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(
				mInflater.inflate(
						R.layout.list_card_view,
						parent,
						false
				)
		);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		final ViewHolder viewHolder = (ViewHolder) holder;
		final SearchTermItem data = getItem(position);

		if (data != null && position >= 0 && position < mCursor.getCount()) {
			viewHolder.bindData(data);
		}
	}

	@Override
	public int getItemCount() {
		return mCursor == null ? 0 : mCursor.getCount();
	}

	public SearchTermItem getItem(final int position) {
		mCursor.moveToPosition(position);

		return SearchTermItem.fromDbCursor(mCursor);
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
		public CardView itemCardView;
		public TextView itemCaption;
		public TextView itemIcon;

		public ViewHolder(View view) {
			super(view);

			itemCardView = (CardView) view.findViewById(R.id.list_card_view);
			itemCaption = (TextView) view.findViewById(R.id.item_description);
			itemIcon = (TextView) view.findViewById(R.id.item_icon);
		}

		public void bindData(final SearchTermItem data) {
			String description = data.getDescription();
			itemCaption.setText(description);

			itemCardView.setOnClickListener((View view) -> {
				mOnItemClick.run(getAdapterPosition());
			});

			itemIcon.setText(
					String.valueOf(description.charAt(1)).toUpperCase()
			);

			itemIcon.getBackground().setColorFilter(
					Color.parseColor(mMaterialTheme.getNextColor().hex),
					PorterDuff.Mode.SRC
			);
		}
	}

	public interface OnItemClick {
		void run(int position);
	}
}
