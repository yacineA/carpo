package com.zunyi.carpo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.*;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.*;
 
import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
public class CarpoActivity extends Activity {

	public static final int GET_EVENTS = Menu.FIRST;
	public static final int GET_ID = Menu.FIRST + 1;
	public static final int LOGOUT = Menu.FIRST + 2;
	private static final String APP_ID = "258295044208268";
	public static final String TAG = "FACEBOOK CONNECT";
	Facebook facebook;
	AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	Button loginButton = null;
	Button cancelButton = null;
	private String userID = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (APP_ID == null || APP_ID.equals("")) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running");
		}

		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);

		loginButton = (Button) findViewById(R.id.loginButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);

		loginButton.setOnClickListener(new LoginButtonListener());

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, CarpoActivity.GET_EVENTS, Menu.NONE, "Get Events");
		menu.add(Menu.NONE, CarpoActivity.GET_ID, Menu.NONE, "Get UserID");
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {	
		MenuItem getEvents = menu.findItem(CarpoActivity.GET_EVENTS);
		MenuItem getID = menu.findItem(CarpoActivity.GET_ID);
		MenuItem logOut = menu.findItem(CarpoActivity.LOGOUT);
		
		if (facebook.isSessionValid()) {
			if(logOut == null)
			{
				menu.add(Menu.NONE, CarpoActivity.LOGOUT, Menu.NONE, "Logout");
			}
			getID.setEnabled(true);
			if (userID != null) {
				getEvents.setEnabled(true);
			} else {
				getEvents.setEnabled(false);
			}

		} else {
			getEvents.setEnabled(false);
			getID.setEnabled(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case CarpoActivity.LOGOUT:
			if (facebook.isSessionValid()) {

				AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
						facebook);
				asyncRunner.logout(this, new LogoutRequestListener());

			break;
		case CarpoActivity.GET_EVENTS:
			this.mAsyncRunner.request("me/events", new EventRequestListener());
			break;
		case CarpoActivity.GET_ID:
			mAsyncRunner.request("me", new IDRequestListener());
			break;
		default:
			return false;
		}
		return true;
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

				facebook.authorize(CarpoActivity.this, new String[] {},
						new DialogListener() {
							@Override
							public void onComplete(Bundle values) {
								SharedPreferences.Editor editor = mPrefs.edit();
								editor.putString("access_token",
										facebook.getAccessToken());
								editor.putLong("access_expires",
										facebook.getAccessExpires());
								editor.commit();
								loginButton.setVisibility(View.INVISIBLE);
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
			else
			{
				loginButton.setVisibility(View.INVISIBLE);
			}
		}

	}
	
	final class IDRequestListener implements RequestListener {
		 
		@Override
		public void onComplete(String response, Object state) {
			try {
				// process the response here: executed in background thread
				Log.d(TAG, "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				final String id = json.getString("id");
 
				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				CarpoActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						userID = id;
						mText.setText("Hello there, " + id + "!");
					}
				}); 
			} catch (JSONException e) {
				Log.w(TAG, "JSON Error in response");
			} catch (FacebookError e) {
				Log.w(TAG, "Facebook Error: " + e.getMessage());
			}
		}
 
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		public void onMalformedURLException1(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
 
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
			
		}
 
	}
 
	private class EventRequestListener implements RequestListener {
 
		@Override
		public void onComplete(String response, Object state) {
			try {
				// process the response here: executed in background thread
				Log.d(TAG, "Response: " + response.toString());
				final JSONObject json = new JSONObject(response);
				JSONArray d = json.getJSONArray("data");
 
				for (int i = 0; i &lt; d.length(); i++) {
					JSONObject event = d.getJSONObject(i);
					FbEvent newEvent = new FbEvent(event.getString("id"),
							event.getString("name"),
							event.getString("start_time"),
							event.getString("end_time"),
							event.getString("location"));
					events.add(newEvent);
 
				}
 
				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				FacebookCon.this.runOnUiThread(new Runnable() {
					public void run() {
						for (FbEvent event : events) {
							TextView view = new TextView(
									getApplicationContext());
							view.setText(event.getTitle());
							view.setTextSize(16);
 
							eventLayout.addView(view);
						}
					}
				});
			} catch (JSONException e) {
				Log.w(TAG, "JSON Error in response");
			}
		}
 
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
 
		}
 
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
 
		}
 
	}
}