package com.zunyi.carpo;


import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailedViewActivity extends Activity {

	TextView detailed_type;
	TextView detailed_from;
	TextView detailed_to;
	TextView detailed_datetime;
	Button detailed_action;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.eventdetailedview);

		detailed_type = (TextView)findViewById(R.id.detailed_type);
		detailed_from = (TextView)findViewById(R.id.detailed_from);
		detailed_to = (TextView)findViewById(R.id.detailed_to);
		detailed_datetime = (TextView)findViewById(R.id.detailed_datetime);
		detailed_action = (Button)findViewById(R.id.detailed_action);
		
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null)
		{
			String type = extras.getString("TYPE");
			detailed_type.setText(type);
			
			String from = extras.getString("TYPE");
			detailed_from.setText("From: "+from);
			
			String to = extras.getString("TYPE");
			detailed_to.setText("To: "+to);
			
			Date time = (Date)extras.getSerializable("START_TIME_DATE");
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-mm-dd hh:mm");
			
			detailed_datetime.setText("Start at: " + sdf.format(time));
			if (type.equals("Offer"))
			{
				detailed_action.setText("Join");
			}
			else 
			{
				detailed_action.setText("Invite");
			}
		}

	}
}
