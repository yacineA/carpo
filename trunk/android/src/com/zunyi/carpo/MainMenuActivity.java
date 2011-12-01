package com.zunyi.carpo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {

	Button carpoolOfferButton = null;
	Button carpoolRequestButton = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.mainmenu);

		carpoolOfferButton = (Button) findViewById(R.id.carpool_offer_button);
		carpoolOfferButton.setOnClickListener(new CarpoolOfferButtonListener());

		carpoolRequestButton = (Button) findViewById(R.id.carpool_request_button);
		carpoolRequestButton
				.setOnClickListener(new CarpoolRequestButtonListener());

	}

	class CarpoolOfferButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(MainMenuActivity.this,
					OfferListActivity.class);
			MainMenuActivity.this.startActivity(myIntent);

		}

	}

	class CarpoolRequestButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(MainMenuActivity.this,
					RequestListActivity.class);
			MainMenuActivity.this.startActivity(myIntent);

		}

	}
}
