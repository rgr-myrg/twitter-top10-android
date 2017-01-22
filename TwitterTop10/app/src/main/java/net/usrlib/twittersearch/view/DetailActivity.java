package net.usrlib.twittersearch.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import net.usrlib.twittersearch.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 1/21/17.
 */

@EActivity(R.layout.detail_activity)
@OptionsMenu(R.menu.menu_main)

public class DetailActivity extends AppCompatActivity {
	@ViewById(R.id.toolbar)
	protected Toolbar mToolbar;

	@AfterViews
	protected void setSupportActionBar() {
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@OptionsItem(R.id.action_settings)
	protected void onMenuSettingsSelected() {
		Log.d("RESULT", "onMenuSettingsSelected");
	}

	@OptionsItem(android.R.id.home)
	void onAndroidHomeSelected() {
		startActivity(new Intent(getApplicationContext(), ListActivity_.class));
	}
}
