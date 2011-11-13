package com.zunyi.carpo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.facebook.android.*;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.*;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CarpoActivity extends Activity {

	private static final String APP_ID = "258295044208268";
	private static final String TAG = "FACEBOOK CONNECT";
	private static final String SERVER_URL_AUTH = "http://70.64.6.83:8080/cmpt412_project/JSPSrv.jsp?";
	Facebook facebook;
	AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";

	Button loginButton = null;

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

		loginButton.setOnClickListener(new LoginButtonListener());

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	private boolean isAccessTokenValid(String userID, String access_token) {
		String isValidString = "";
		try {

			URL url = new URL(SERVER_URL_AUTH + "id=" + userID + "&token="
					+ access_token);
			Log.d(TAG, url.toString());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("Message");

			Element nameElement = (Element) nodeList.item(0);
			nodeList = nameElement.getChildNodes();
			isValidString = ((Node) nodeList.item(0)).getNodeValue();
			Log.d(TAG, isValidString);
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		boolean isValid = false;
		if (isValidString.trim().equals("true")) {
			isValid = true;
		}
		return isValid;
	}

	class LoginButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			/*
			 * Only call authorize if the access_token has expired.
			 */

			facebook.authorize(CarpoActivity.this, new String[] {},
					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							
							AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
									facebook);
							asyncRunner.request("me", new IDRequestListener());
							
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

	}

	class IDRequestListener implements RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			try {
				// process the response here: executed in background thread
				Log.d(TAG, "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				final String id = json.getString("id");

				if (CarpoActivity.this.isAccessTokenValid(id,
						facebook.getAccessToken())) {

					CarpoActivity.this.runOnUiThread(new Runnable() {
						public void run() {

							Intent myIntent = new Intent(CarpoActivity.this,
									SuggestionActivity.class);
							CarpoActivity.this.startActivity(myIntent);
						}
					});
				} else {
					CarpoActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Util.showAlert(CarpoActivity.this, "Warning",
									"Token Invalid");
						}
					});

				}

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
	
	class LogoutRequestListener implements RequestListener {

		@Override
		public void onComplete(String response, Object state) {

			loginButton.setVisibility(View.VISIBLE);
			// Dispatch on its own thread

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