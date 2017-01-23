package net.usrlib.twittersearch.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.usrlib.twittersearch.BuildConfig;
import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.SearchTermItem;
import net.usrlib.twittersearch.presenter.Presenter;
import net.usrlib.twittersearch.service.SearchUpdateService;
import net.usrlib.twittersearch.util.DbHelper;
import net.usrlib.twittersearch.util.UiUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by rgr-myrg on 1/22/17.
 */

@EFragment(R.layout.detail_fragment_main)

public class DetailFragment extends Fragment {
	public static final String TAG = DetailFragment.class.getSimpleName();
	public static final int TIMELAPSE_MINUTES = 1;

	@ViewById(R.id.detail_recycler_view)
	protected RecyclerView mRecyclerView;

	@ViewById(R.id.detail_recycler_empty)
	protected TextView mEmptyListMessage;

	@ViewById(R.id.detail_swipe_refresh_layout)
	protected SwipeRefreshLayout mSwipeRefreshLayout;

	protected ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
	protected ScheduledFuture<?> mScheduledFuture;
	protected DetailAdapter mRecyclerAdapter = null;
	protected int mSearchItemId;
	protected String mSearchTerm;

	@AfterViews
	protected void startUp() {
		final Intent intent = getActivity().getIntent();

		mSearchItemId = intent.getIntExtra(SearchTermItem.ITEM_ID_COLUMN, DbHelper.NO_ID);
		mSearchTerm = intent.getStringExtra(SearchTermItem.DESCRIPTION_COLUMN);

		if (BuildConfig.DEBUG) {
			Log.i(TAG, mSearchItemId + ":" + mSearchTerm);
		}

		getActivity().registerReceiver(
				mBroadcastReceiver,
				new IntentFilter(SearchUpdateService.STATE_CHANGE)
		);

		mSwipeRefreshLayout.setOnRefreshListener(() -> {
			requestDataRefresh();
		});

		fetchDataFromDb();
	}

	@Override
	public void onStop() {
		super.onStop();

		if (mScheduledFuture == null) {
			return;
		}

		if (!mScheduledFuture.isDone()) {
			mScheduler.shutdown();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		scheduleService();
	}

	protected void fetchDataFromDb() {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "Fetching data from Db");
		}

		final Cursor cursor = Presenter.getResultItemsFromDb(getContext(), mSearchItemId);

		if (cursor == null) {
			return;
		}

		if (cursor.getCount() > 0) {
			mEmptyListMessage.setVisibility(View.GONE);
		}

		initRecyclerViewAndAdapter(cursor);
	}

	protected void requestDataRefresh() {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "Requesting data refresh.");
		}

		getActivity().startService(
				new Intent(getContext(), SearchUpdateService.class)
						.putExtra(SearchTermItem.ITEM_ID_COLUMN, mSearchItemId)
						.putExtra(SearchTermItem.DESCRIPTION_COLUMN, mSearchTerm)
		);
	}

	protected void scheduleService() {
		mScheduledFuture = mScheduler.scheduleAtFixedRate(() -> {
			requestDataRefresh();
		}, 0, TIMELAPSE_MINUTES, TimeUnit.MINUTES);
	}

	protected void initRecyclerViewAndAdapter(final Cursor cursor) {
		mRecyclerAdapter = new DetailAdapter(getContext(), cursor, position -> {
		});

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(mRecyclerAdapter);

		mSwipeRefreshLayout.setRefreshing(false);
	}

	protected BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(SearchUpdateService.STATE_CHANGE)) {
				return;
			}

			final int serviceStatus = intent.getIntExtra(
					SearchUpdateService.STATUS_UPDATE, -1
			);

			Log.i(TAG, "onReceive serviceStatus: " + serviceStatus);

			switch (serviceStatus) {
				case SearchUpdateService.STATUS_NOT_STARTED:
					break;

				case SearchUpdateService.STATUS_STARTED:
					UiUtil.makeSnackbar(getView(), getString(R.string.refresh_message));
					break;

				case SearchUpdateService.STATUS_COMPLETE:
					mSwipeRefreshLayout.setRefreshing(false);
					fetchDataFromDb();
					break;
			}
		}
	};
}
