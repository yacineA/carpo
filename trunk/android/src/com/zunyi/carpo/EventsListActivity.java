package com.zunyi.carpo;

import java.util.ArrayList;
import java.util.Calendar;

import com.zunyi.carpo.model.Event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class EventsListActivity extends BaseEventListActivity {

	private int mYear, mMonth, mDay;
	static final int ID_DATEPICKER = 0;

	Button dateEditButton;
	Button timeEditButton;
	EditText fromEditText;
	EditText toEditText;
	
	private int searchBtnId = Menu.FIRST;
	private int scheduleBtnId = Menu.FIRST + 1;
	private int group1Id = 1;
	private int group2Id = 2;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist);

		dateEditButton = (Button) findViewById(R.id.dateEditButton);
		timeEditButton = (Button) findViewById(R.id.timeEditButton);
		fromEditText = (EditText) findViewById(R.id.fromEditText);
		toEditText = (EditText) findViewById(R.id.toEditText);

		dateEditButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(ID_DATEPICKER);
				
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		updateDisplay();
		
	}
	
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {   
	    menu.add(group1Id,searchBtnId ,searchBtnId,"Add");   
	    menu.add(group2Id,scheduleBtnId ,scheduleBtnId,"Get Events Near Me");   
	    // the following line will hide search    
	    // when we turn the 2nd parameter to false   
	    //menu.setGroupVisible(1, false);   
	    return super.onCreateOptionsMenu(menu);   
	  }   
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}

	};

	private void updateDisplay() {
		dateEditButton.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}
	
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case ID_DATEPICKER:
	        return new DatePickerDialog(this,
	                    mDateSetListener,
	                    mYear, mMonth, mDay);
	    }
	    return null;
	}
	

	
}
