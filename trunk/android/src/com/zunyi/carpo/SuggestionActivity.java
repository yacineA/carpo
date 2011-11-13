package com.zunyi.carpo;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class SuggestionActivity extends ListActivity  {
	
	private static final String TAG = "SUGGESTION_ACTIVITY";
	
	Button skipButton = null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.suggestion);
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				"Linux", "OS/2" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		
		skipButton = (Button)findViewById(R.id.skipButton);
		skipButton.setOnClickListener(new OnSkipButtonPressedListner());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG);
	}

	private class OnSkipButtonPressedListner implements OnClickListener
	{

		@Override
		public void onClick(View v) {

			Intent myIntent = new Intent(SuggestionActivity.this,
					MainMenuActivity.class);
			SuggestionActivity.this.startActivity(myIntent);
			finish();
		}
		
	}
	
}
