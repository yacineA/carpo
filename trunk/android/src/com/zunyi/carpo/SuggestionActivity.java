package com.zunyi.carpo;

import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import com.openenviron.andeasylib.EasyLocation;
import com.zunyi.carpo.model.Event;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.Button;




public class SuggestionActivity extends BaseEventListActivity {

	private static final int offerSuggestionCount = 8;
	private static final int requestSuggestionCount = 8;

	Button skipButton = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.suggestion);

		SERVER_URL = "http://70.64.6.83:8080/cmpt412_project/suggestions?"
				+ "countOffer="
				+ offerSuggestionCount
				+ "&countRequest="
				+ requestSuggestionCount;
		TAG = "SUGGESTION_ACTIVITY";
		
		contentXMLNodeElement = "Suggestion";
		

		skipButton = (Button) findViewById(R.id.skipButton);
		skipButton.setOnClickListener(new OnSkipButtonPressedListner());


		getEventsFromServer();
	}



	private class OnSkipButtonPressedListner implements OnClickListener {

		@Override
		public void onClick(View v) {

			Intent myIntent = new Intent(SuggestionActivity.this,
					MainMenuActivity.class);
			SuggestionActivity.this.startActivity(myIntent);
			EasyLocation.stopGPS();
			unregisterReceiver(br);
			finish();
		}

	}

}
