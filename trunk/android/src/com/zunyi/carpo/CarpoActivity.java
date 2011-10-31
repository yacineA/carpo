package com.zunyi.carpo;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class CarpoActivity extends Activity {

	Facebook facebook = new Facebook("258295044208268");
	//AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	Button loginButton = null;
	Button cancelButton = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		loginButton = (Button) findViewById(R.id.loginButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);

		loginButton.setOnClickListener(new LoginButtonListener());

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	class LoginButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			/*
			 * Get existing access_token if any
			 */
			mPrefs = getPreferences(MODE_PRIVATE);
			String access_token = mPrefs.getString("access_token", null);
			long expires = mPrefs.getLong("access_expires", 0);
			if (access_token != null) {
				facebook.setAccessToken(access_token);
			}
			if (expires != 0) {
				facebook.setAccessExpires(expires);
			}

			/*
			 * Only call authorize if the access_token has expired.
			 */
			if (!facebook.isSessionValid()) {

				facebook.authorize(CarpoActivity.this, new String[] {}, new DialogListener() {
					@Override
					public void onComplete(Bundle values) {
						System.out.println(values.getString("response"));
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putString("access_token", facebook.getAccessToken());
						editor.putLong("access_expires",
								facebook.getAccessExpires());
						editor.commit();
					}

					@Override
					public void onFacebookError(FacebookError error) {
					}

					@Override
					public void onError(DialogError e) {
					}

					@Override
					public void onCancel() {
					}
				});
			}
		}

	}
}