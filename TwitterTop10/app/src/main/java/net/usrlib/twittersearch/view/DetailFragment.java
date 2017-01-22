package net.usrlib.twittersearch.view;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.SearchTermItem;
import net.usrlib.twittersearch.presenter.Presenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 1/21/17.
 */

@EFragment(R.layout.detail_fragment_main)

public class DetailFragment extends Fragment {
	public static final String TAG = DetailFragment.class.getSimpleName();

	@ViewById(R.id.detail_recycler_view)
	protected RecyclerView mRecyclerView;

	@ViewById(R.id.detail_recycler_empty)
	protected TextView mEmptyListMessage;

	@ViewById(R.id.detail_swipe_refresh_layout)
	protected SwipeRefreshLayout mSwipeRefreshLayout;

	protected DetailAdapter mRecyclerAdapter = null;

	@AfterViews
	protected void onAfterViews() {
		final Intent intent = getActivity().getIntent();
		final int searchItemId  = intent.getIntExtra(SearchTermItem.ITEM_ID_COLUMN, -1);
		final String searchTerm = intent.getStringExtra(SearchTermItem.DESCRIPTION_COLUMN);

		Log.i("DETAIL", searchItemId + ":" + searchTerm);
		final Cursor cursor = Presenter.getResultItemsFromDb(getContext(), searchItemId);

		if (cursor == null) {
			return;
		}

		if (cursor.getCount() > 0) {
			mEmptyListMessage.setVisibility(View.GONE);
		}

		initRecyclerViewAndAdapter(cursor);

		mSwipeRefreshLayout.setOnRefreshListener(() -> {
			Presenter.startSearchRequest(getContext(), searchItemId, searchTerm, response -> {
				final int resultType = response.getType();

				if (resultType == Presenter.SearchResult.SUCCESS) {
					onRequestNewSearchItemComplete(
							response.getSearchId(),
							response.getSearchTerm()
					);
				}
			});
		});
	}

	protected void onRequestNewSearchItemComplete(final int itemId, final String searchTerm) {
		Log.i("DETAIL", "-> " + itemId + ":" + searchTerm);
		mSwipeRefreshLayout.setRefreshing(false);
	}

	private void initRecyclerViewAndAdapter(final Cursor cursor) {
		if (mRecyclerAdapter != null) {
			mRecyclerAdapter.changeCursor(cursor);
			return;
		}

		Log.i(TAG, "initRecyclerViewAndAdapter : " + cursor.getCount());
		mRecyclerAdapter = new DetailAdapter(getContext(), cursor, position -> {
		});

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(mRecyclerAdapter);
	}
}
