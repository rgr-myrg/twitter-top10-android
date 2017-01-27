package net.usrlib.twittersearch.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.usrlib.material.MaterialTheme;
import net.usrlib.material.Theme;
import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.SearchTermItem;
import net.usrlib.twittersearch.presenter.Presenter;
import net.usrlib.twittersearch.touch.ItemTouchHelperAdapter;
import net.usrlib.twittersearch.touch.ItemTouchHelperViewHolder;
import net.usrlib.twittersearch.touch.OnStartDragListener;
import net.usrlib.twittersearch.util.Debug;

/**
 * Created by rgr-myrg on 1/22/17.
 */

public class ListAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {
	public static final String TAG = ListAdapter.class.getSimpleName();

	protected final MaterialTheme mMaterialTheme = MaterialTheme.get(Theme.TOPAZ);
	protected LayoutInflater mInflater = null;
	protected Context mContext = null;
	protected Cursor mCursor = null;
	protected OnItemClick mOnItemClick = null;
	protected OnStartDragListener mDragStartListener = null;

	protected boolean mItemIsMoving = false;

	protected int mItemStartPosition = -1;
	protected int mItemTargetPosition = -1;


	public ListAdapter(
			final Context context,
			final Cursor cursor,
			final OnItemClick callback,
			final OnStartDragListener dragListener) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mCursor = cursor;
		this.mOnItemClick = callback;
		this.mDragStartListener = dragListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ViewHolder viewHolder = new ViewHolder(
				mInflater.inflate(
						//R.layout.list_card_view,
						R.layout.list_item_data,
						parent,
						false
				)
		);

//		viewHolder.itemView.setOnTouchListener((view, motionEvent) -> {
//			Debug.i(TAG, "onTouchListener event: " + motionEvent);
//			switch (motionEvent.getAction()) {
//				case DragEvent.ACTION_DRAG_ENDED:
//					Debug.i(TAG, "onTouchListener ACTION_DRAG_ENDED");
//					break;
//				case DragEvent.ACTION_DROP:
//					Debug.i(TAG, "onTouchListener ACTION_DROP");
//					break;
//				case DragEvent.ACTION_DRAG_EXITED:
//					Debug.i(TAG, "onTouchListener ACTION_DRAG_EXITED");
//					break;
//			}
//
//			return false;
//		});

		return viewHolder;
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

	/*
	 * ItemTouchHelperAdapter method
	 */
	@Override
	public boolean onItemMove(int fromPosition, int toPosition) {
		mItemIsMoving = true;
		mItemStartPosition = getItem(fromPosition).getItemId();
		mItemTargetPosition = getItem(toPosition).getItemId();

		notifyItemMoved(fromPosition, toPosition);

		return false;
	}

	@Override
	public void onItemStateIdle() {
		Debug.i(TAG, "onItemStateIdle"  + mItemStartPosition + "->" + mItemTargetPosition);

		if (!mItemIsMoving) {
			return;
		}

		mItemIsMoving = false;

		final Cursor cursorUpdate = Presenter.swapSearchItemsInDb(
				mContext, mItemStartPosition, mItemTargetPosition
		);

		if (cursorUpdate == mCursor) {
			return;
		}

		final Cursor previousCursor = mCursor;

		mCursor = cursorUpdate;

		if (mCursor != null) {
			Debug.i(TAG, "notifyDataSetChanged");
			notifyDataSetChanged();
		}

		if (previousCursor != null) {
			previousCursor.close();
		}
	}

	/*
	 * ItemTouchHelperAdapter method
	 */
	@Override
	public void onItemDismiss(int position) {
		final Cursor cursorUpdate = Presenter.deleteSearchItemFromDb(
				mContext,
				getItem(position).getItemId()
		);

		if (cursorUpdate == mCursor) {
			return;
		}

		final Cursor previousCursor = mCursor;

		mCursor = cursorUpdate;

		if (mCursor != null) {
			notifyItemRemoved(position);
		}

		if (previousCursor != null) {
			previousCursor.close();
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
		public LinearLayout itemDetails;
		public TextView itemCaption;
		public TextView itemIcon;
		public ImageView itemDragHandle;

		public ViewHolder(View view) {
			super(view);

			itemDetails = (LinearLayout) view.findViewById(R.id.list_item_details);
			itemCaption = (TextView) view.findViewById(R.id.item_description);
			itemIcon = (TextView) view.findViewById(R.id.item_icon);
			itemDragHandle = (ImageView) view.findViewById(R.id.item_drag_handle);
		}

		public void bindData(final SearchTermItem data) {
			String description = data.getDescription();
			itemCaption.setText(description);

			itemDetails.setOnClickListener((View view) -> {
				mOnItemClick.run(getAdapterPosition());
			});

			itemIcon.setText(
					String.valueOf(description.charAt(1)).toUpperCase()
			);

			itemIcon.getBackground().setColorFilter(
					Color.parseColor(mMaterialTheme.getNextColor().hex),
					PorterDuff.Mode.SRC
			);

			itemDragHandle.setOnTouchListener((view, event) -> {
				Debug.i(TAG, "dragHandle onTouch event: " + event.getAction());
				// TODO: Comment out until sorting issue in DB is fixed.
//				if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//					mDragStartListener.onStartDrag(this);
//				}
				return false;
			});
		}

		/*
		 * ItemTouchHelperViewHolder method
		 */
		@Override
		public void onItemSelected() {
			this.itemView.setBackgroundColor(Color.LTGRAY);
		}

		/*
		 * ItemTouchHelperViewHolder method
		 */
		@Override
		public void onItemClear() {
			this.itemView.setBackgroundColor(0);
		}
	}

	public interface OnItemClick {
		void run(int position);
	}
}
