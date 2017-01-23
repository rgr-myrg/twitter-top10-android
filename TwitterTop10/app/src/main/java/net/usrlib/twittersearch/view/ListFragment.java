package net.usrlib.twittersearch.view;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

@EFragment(R.layout.list_fragment_main)

public class ListFragment extends Fragment {
	public static final String TAG = ListFragment.class.getSimpleName();

	@ViewById(R.id.list_recycler_view)
	protected RecyclerView mRecyclerView;

	@ViewById(R.id.list_recycler_empty)
	protected TextView mEmptyListMessage;

	protected ListAdapter mRecyclerAdapter = null;

	@AfterViews
	protected void onAfterViews() {
		final Cursor cursor = Presenter.getSearchItemsFromDb(getContext());

		if (cursor == null) {
			return;
		}

		if (cursor.getCount() > 0) {
			mEmptyListMessage.setVisibility(View.GONE);
		}

		initRecyclerViewAndAdapter(cursor);
	}

	private void initRecyclerViewAndAdapter(final Cursor cursor) {
		mRecyclerAdapter = new ListAdapter(getContext(), cursor, position -> {
			startDetailActivity(position);
		});

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(mRecyclerAdapter);
	}

	private void startDetailActivity(int position) {
		final SearchTermItem item = mRecyclerAdapter.getItem(position);
		final Intent intent = new Intent(getContext(), DetailActivity_.class);

		intent.putExtra(SearchTermItem.ITEM_ID_COLUMN, item.getItemId());
		intent.putExtra(SearchTermItem.DESCRIPTION_COLUMN, item.getDescription());

		getContext().startActivity(intent);
	}
}
