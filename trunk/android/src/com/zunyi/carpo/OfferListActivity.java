package com.zunyi.carpo;

import android.os.Bundle;


public class OfferListActivity extends EventsListActivity {


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		SERVER_URL = "http://70.64.6.83:8080/cmpt412_project/offers_out?"
		+ "count=1";
		TAG = "OFFER_ACTIVITY";
		
		contentXMLNodeElement = "Offer";
		
		getEventsFromServer();
	}


}
