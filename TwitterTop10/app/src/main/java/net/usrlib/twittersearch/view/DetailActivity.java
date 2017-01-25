package net.usrlib.twittersearch.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.SearchTermItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 1/21/17.
 */

@EActivity(R.layout.detail_activity)

public class DetailActivity extends AppCompatActivity {
	@ViewById(R.id.toolbar)
	protected Toolbar mToolbar;

	@AfterViews
	protected void setSupportActionBar() {
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final Intent intent = getIntent();

		if (intent == null) {
			return;
		}

		final String searchTerm = intent.getStringExtra(SearchTermItem.DESCRIPTION_COLUMN);

		if (searchTerm == null) {
			return;
		}

		getSupportActionBar().setTitle(searchTerm);
	}

	@OptionsItem(android.R.id.home)
	void onAndroidHomeSelected() {
		startActivity(new Intent(getApplicationContext(), ListActivity_.class));
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		onAndroidHomeSelected();
	}
}
