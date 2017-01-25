package net.usrlib.twittersearch.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import net.usrlib.twittersearch.R;
import net.usrlib.twittersearch.model.TwitterSearchResponse;
import net.usrlib.twittersearch.util.Preferences;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 1/23/17.
 */

@EActivity(R.layout.settings_activity)

public class SettingsActivity extends AppCompatActivity {
	@ViewById(R.id.toolbar)
	protected Toolbar mToolbar;

	@ViewById(R.id.radio_most_recent)
	protected RadioButton mRadioMostRecent;

	@ViewById(R.id.radio_most_popular)
	protected RadioButton mRadioMostPopular;

	@ViewById(R.id.radio_mixed)
	protected RadioButton mRadioMixed;

	@ViewById(R.id.radio_frequency_high)
	protected RadioButton mRadioFrequencyHigh;

	@ViewById(R.id.radio_frequency_medium)
	protected RadioButton mRadioFrequencyMedium;

	@ViewById(R.id.radio_frequency_low)
	protected RadioButton mRadioFrequencyLow;

	@ViewById(R.id.radio_count_high)
	protected RadioButton mRadioCountHigh;

	@ViewById(R.id.radio_count_medium)
	protected RadioButton mRadioCountMedium;

	@ViewById(R.id.radio_count_low)
	protected RadioButton mRadioCountLow;

	@AfterViews
	protected void setSupportActionBar() {
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@AfterViews
	protected void checkButtonsFromPreferences() {
		final String searchType = Preferences.getSearchType(getApplicationContext());

		if (searchType.equals(TwitterSearchResponse.SEARCH_TYPE_POPULAR)) {
			mRadioMostPopular.setChecked(true);

		} else if (searchType.equals(TwitterSearchResponse.SEARCH_TYPE_MIXED)) {
			mRadioMixed.setChecked(true);

		} else {
			mRadioMostRecent.setChecked(true);
		}

		final int frequency = Preferences.getRefreshFrequency(getApplicationContext());

		switch (frequency) {
			case TwitterSearchResponse.FREQUENCY_HIGH:
				mRadioFrequencyHigh.setChecked(true);
				break;

			case TwitterSearchResponse.FREQUENCY_MEDIUM:
				mRadioFrequencyMedium.setChecked(true);
				break;

			case TwitterSearchResponse.FREQUENCY_LOW:
				mRadioFrequencyLow.setChecked(true);
				break;
		}

		final int count = Preferences.getSearchCount(getApplicationContext());

		switch (count) {
			case TwitterSearchResponse.SEARCH_COUNT_HIGH:
				mRadioCountHigh.setChecked(true);
				break;
			case TwitterSearchResponse.SEARCH_COUNT_MEDIUM:
				mRadioCountMedium.setChecked(true);
				break;
			case TwitterSearchResponse.SEARCH_COUNT_LOW:
				mRadioCountLow.setChecked(true);
				break;
		}
	}

	@OptionsItem(android.R.id.home)
	void onAndroidHomeSelected() {
		startActivity(new Intent(getApplicationContext(), ListActivity_.class));
		finish();
	}

	public void onRadioButtonClicked(View view) {
		boolean isChecked = ((RadioButton) view).isChecked();

		if (!isChecked) {
			return;
		}

		switch (view.getId()) {
			case R.id.radio_most_recent:
				Preferences.setSearchType(
						getApplicationContext(),
						TwitterSearchResponse.SEARCH_TYPE_RECENT
				);
				break;
			case R.id.radio_most_popular:
				Preferences.setSearchType(
						getApplicationContext(),
						TwitterSearchResponse.SEARCH_TYPE_POPULAR
				);
				break;
			case R.id.radio_mixed:
				Preferences.setSearchType(
						getApplicationContext(),
						TwitterSearchResponse.SEARCH_TYPE_MIXED
				);
				break;
			case R.id.radio_frequency_high:
				Preferences.setRefreshFrequency(
						getApplicationContext(),
						TwitterSearchResponse.FREQUENCY_HIGH
				);
				break;
			case R.id.radio_frequency_medium:
				Preferences.setRefreshFrequency(
						getApplicationContext(),
						TwitterSearchResponse.FREQUENCY_MEDIUM
				);
				break;
			case R.id.radio_frequency_low:
				Preferences.setRefreshFrequency(
						getApplicationContext(),
						TwitterSearchResponse.FREQUENCY_LOW
				);
				break;
			case R.id.radio_count_high:
				Preferences.setSearchCount(
						getApplicationContext(),
						TwitterSearchResponse.SEARCH_COUNT_HIGH
				);
				break;
			case R.id.radio_count_medium:
				Preferences.setSearchCount(
						getApplicationContext(),
						TwitterSearchResponse.SEARCH_COUNT_MEDIUM
				);
				break;
			case R.id.radio_count_low:
				Preferences.setSearchCount(
						getApplicationContext(),
						TwitterSearchResponse.SEARCH_COUNT_LOW
				);
				break;
		}
	}
}
