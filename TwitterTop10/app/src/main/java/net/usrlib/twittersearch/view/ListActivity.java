package net.usrlib.twittersearch.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import net.usrlib.twittersearch.BuildConfig;
import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.SearchTermItem;
import net.usrlib.twittersearch.presenter.Presenter;
import net.usrlib.twittersearch.util.DbHelper;
import net.usrlib.twittersearch.util.UiUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import static net.usrlib.twittersearch.presenter.Presenter.NO_RESULTS_FOUND;

/**
 * Created by rgr-myrg on 1/21/17.
 */

@EActivity(R.layout.list_activity)
@OptionsMenu(R.menu.menu_main)

public class ListActivity extends AppCompatActivity {
	@ViewById(R.id.toolbar)
	protected Toolbar mToolbar;

	@ViewById(R.id.progress_bar)
	protected ProgressBar mProgressBar;

	@ViewById(android.R.id.content)
	protected View mRootView;

	@AfterViews
	protected void setSupportActionBar() {
		setSupportActionBar(mToolbar);
	}

	@OptionsItem(R.id.action_settings)
	protected void onMenuSettingsSelected() {
		startActivity(new Intent(getApplicationContext(), SettingsActivity_.class));
	}

	// Floating Button Handler
	public void onAddSearchItemClick(View view) {
		UiUtil.showNewItemAlertDialog(view.getContext(), null, value -> {
			mProgressBar.setVisibility(View.VISIBLE);
			requestNewSearchItem(value);
		});
	}

	@Background
	protected void requestNewSearchItem(final String description) {
		Presenter.startSearchRequest(getApplicationContext(), DbHelper.NO_ID, description, results -> {
			final int resultType = results.getType();

			if (resultType == Presenter.SearchResult.SUCCESS) {
				onRequestNewSearchItemComplete(
						results.getSearchId(),
						results.getSearchTerm()
				);
			} else {
				onRequestNewSearchItemError(
						results.getStatus(),
						results.getMessage()
				);
			}
		});
	}

	@UiThread
	protected void onRequestNewSearchItemComplete(final int itemId, final String searchTerm) {
		mProgressBar.setVisibility(View.INVISIBLE);

		final Intent intent = new Intent(getApplicationContext(), DetailActivity_.class);

		intent.putExtra(SearchTermItem.ITEM_ID_COLUMN, itemId);
		intent.putExtra(SearchTermItem.DESCRIPTION_COLUMN, searchTerm);

		startActivity(intent);
	}

	@UiThread
	protected void onRequestNewSearchItemError(final int status, final String message) {
		if (BuildConfig.DEBUG) {
			Log.e("ERROR", "Status: " + status);
		}

		mProgressBar.setVisibility(View.INVISIBLE);

		String uiMsg = message;

		switch (status) {
			case NO_RESULTS_FOUND:
				uiMsg = getString(R.string.search_no_results);
				break;
			default:
				uiMsg = getString(R.string.search_results_error);
				break;
		}

		UiUtil.makeSnackbar(mRootView, uiMsg);
	}
}
