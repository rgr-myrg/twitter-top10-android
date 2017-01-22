package net.usrlib.twittersearch.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import net.usrlib.twittersearch.BuildConfig;
import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.presenter.Presenter;
import net.usrlib.twittersearch.util.UiUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 1/21/17.
 */

@EActivity(R.layout.list_activity)
@OptionsMenu(R.menu.menu_main)

public class ListActivity extends AppCompatActivity {
	@ViewById(R.id.toolbar)
	protected Toolbar mToolbar;

	@AfterViews
	protected void setSupportActionBar() {
		setSupportActionBar(mToolbar);
	}

	@OptionsItem(R.id.action_settings)
	protected void onMenuSettingsSelected() {
		Log.d("LIST", "onMenuSettingsSelected");
	}

	// Floating Button Handler
	public void onAddSearchItemClick(View view) {
		UiUtil.showNewItemAlertDialog(view.getContext(), null, value -> {
			requestNewSearchItem(value);
		});
	}

	@Background
	protected void requestNewSearchItem(final String description) {
		Presenter.startNewSearch(getApplicationContext(), description, results -> {
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
	protected void onRequestNewSearchItemComplete(final int searchId, final String searchTerm) {
		Log.d("LIST", "onRequestNewSearchItemComplete: " + searchId + " : " + searchTerm);
	}

	@UiThread
	protected void onRequestNewSearchItemError(final int status, final String message) {
		if (BuildConfig.DEBUG) {
			Log.e("ERROR", "Status: " + status + " : " + message);
		}
	}
}
/*
public static final String AUTH_RESPONSE_ERROR = "AuthResponse error";
	public static final String EMPTY_SEARCH_TERM = "Search term invalid";
	public static final String NO_RESULTS_FOUND = "No results found";
	public static final String SEARCH_NOT_ADDED = "Unable to add new search";
 */
