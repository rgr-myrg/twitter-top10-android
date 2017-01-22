package net.usrlib.twittersearch.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import net.usrlib.twittersearch.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.WindowFeature;

/**
 * Created by rgr-myrg on 1/21/17.
 */

@Fullscreen
@EActivity(R.layout.splash_screen_activity)
@WindowFeature(Window.FEATURE_NO_TITLE)

public class SplashScreenActivity extends AppCompatActivity {
	@AfterViews
	protected void startNextActivity() {
		startActivity(
				new Intent(getBaseContext(), ListActivity_.class)
		);

		finish();
	}
}
