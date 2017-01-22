package net.usrlib.twittersearch.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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

	public void onAddSearchItemClick(View view) {
		UiUtil.showNewItemAlertDialog(view.getContext(), null, value -> {
			Log.d("LIST", "onAddSearchItemClick: " + value);
			requestNewSearchItem(value);
		});
	}

	@Background
	protected void requestNewSearchItem(final String description) {
		Presenter.addNewSearchItem(getApplicationContext(), description, rowId -> {
			onRequestNewSearchItemComplete(rowId);
		});
	}

	@UiThread
	protected void onRequestNewSearchItemComplete(int rowId) {
		Log.d("LIST", "onAddSearchItemClick: " + rowId);
	}
}
