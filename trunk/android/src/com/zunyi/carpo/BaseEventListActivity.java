package com.zunyi.carpo;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import com.openenviron.andeasylib.EasyLocation;
import com.zunyi.carpo.model.Event;
import com.zunyi.carpo.model.Offer;
import com.zunyi.carpo.model.Request;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BaseEventListActivity extends ListActivity {
	
	ProgressDialog mSpinner;
	ArrayList<Event> events = null;
	EventListAdaptor eventListAdaptor = null;
	Runnable viewEvents;
	String curLatitudeString = null;
	String curLongitudeString = null;
	String contentXMLNodeElement = null;
	BroadcastReceiver br;
	
	static String SERVER_URL;
	static String TAG;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		// Start The GPS Service by passing it the main activity.
		EasyLocation.startGPS(this);

		events = new ArrayList<Event>();
		eventListAdaptor = new EventListAdaptor(this, R.layout.eventlistrow,
				events);
		setListAdapter(eventListAdaptor);
		
		// Create a Broadcast Reciver for using the library, it is needed for
		// every module, as all the available data is brodcasted as intents.
		br = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// The Latitude data is available as string Extra from the
				// intent.
				BaseEventListActivity.this.curLatitudeString = intent
						.getStringExtra(EasyLocation.LATITUDE);
				BaseEventListActivity.this.curLongitudeString = intent
						.getStringExtra(EasyLocation.LONGITUDE);	

			}
		};
		

		// Register the Broadcast receiver and use an intent Filter passing in
		// the AND_EASY_LIB. this filter is universal for the library.
		registerReceiver(br, new IntentFilter(EasyLocation.AND_EASY_LIB));
		
		mSpinner = new ProgressDialog(this);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");

		mSpinner.show();
		
	}
	

	@Override
    protected void onResume()
	{
		// the AND_EASY_LIB. this filter is universal for the library.
		registerReceiver(br, new IntentFilter(EasyLocation.AND_EASY_LIB));
	}
	
	@Override
	protected void onPause()
	{
		unregisterReceiver(br);
	}
	
	@Override
	protected void onStop()
	{

		unregisterReceiver(br);
		
	}
	
	@Override
	protected void onDestroy()
	{

		unregisterReceiver(br);
	}
	
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			System.out.println("events.size()" + events.size());
			if (events != null && events.size() > 0) {
				eventListAdaptor.clear();
				for (int i = 0; i < events.size(); i++) {
					eventListAdaptor.add(events.get(i));
				}
			}
			mSpinner.dismiss();
			eventListAdaptor.notifyDataSetChanged();
		}
	};

	void getListContent() {

		try {
			events = new ArrayList<Event>();
			URL url;
			String urlString = getContentURL();

			Log.d(TAG, urlString);
			url = new URL(urlString);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList eventList = doc.getElementsByTagName(contentXMLNodeElement);
			int totalEvents = eventList.getLength();
			System.out.println("Total no of suggestions : " + totalEvents);

			for (int s = 0; s < totalEvents; s++) {

				Node eventNode = eventList.item(s);
				if (eventNode.getNodeType() == Node.ELEMENT_NODE) {

					Event event = getEventFrom(eventNode);

					events.add(event);

				}// end of if clause

			}// end of for loop with s var

		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		runOnUiThread(returnRes);
		
		
	}


	void getEventsFromServer() {
		viewEvents = new Runnable() {
			@Override
			public void run() {
				getListContent();
			}
		};

		Thread thread = new Thread(null, viewEvents,
				"MagentoBackground");
		thread.start();
	}

	
	Event getEventFrom(Node eventNode) throws ParseException {
		Event event;
		Element suggestionElement = (Element) eventNode;

		// -------
		NodeList typeList = suggestionElement
				.getElementsByTagName("Type");
		Element typeElement = (Element) typeList.item(0);

		NodeList textTypeList = typeElement.getChildNodes();

		if (textTypeList.equals("Offer")) {
			event = new Offer();

			// ----
			NodeList capacityList = suggestionElement
					.getElementsByTagName("Capacity");
			Element capacityElement = (Element) capacityList
					.item(0);

			NodeList textCapacityList = capacityElement
					.getChildNodes();
			int capacity = Integer
					.parseInt(((Node) textCapacityList.item(0))
							.getNodeValue().trim());
			// System.out.println("Capacity : " +
			// ((Node)textCapacityList.item(0)).getNodeValue().trim());
			((Offer) event).setCapacity(capacity);
			// ------

			// ----
			NodeList sharedList = suggestionElement
					.getElementsByTagName("Shared");
			Element sharedElement = (Element) sharedList.item(0);

			NodeList textSharedList = sharedElement.getChildNodes();
			boolean isShared = ((Node) textSharedList.item(0))
					.getNodeValue().trim().equals(1) ? true : false;
			((Offer) event).setShared(isShared);
			// System.out.println("Shared : " +
			// ((Node)textSharedList.item(0)).getNodeValue().trim());

			// ------
		} else {
			event = new Request();

		}
		// System.out.println("Type : " +
		// ((Node)textTypeList.item(0)).getNodeValue().trim());

		// -------
		NodeList idList = suggestionElement
				.getElementsByTagName("ID");
		Element idElement = (Element) idList.item(0);

		NodeList textIDList = idElement.getChildNodes();
		int id = Integer.parseInt(((Node) textIDList.item(0))
				.getNodeValue().trim());
		// System.out.println("ID : " +
		// ((Node)textIDList.item(0)).getNodeValue().trim());
		event.setId(id);

		// ----
		NodeList creatorList = suggestionElement
				.getElementsByTagName("Creator");
		Element creatorElement = (Element) creatorList.item(0);

		NodeList textCreatorList = creatorElement.getChildNodes();
		int creator = Integer.parseInt(((Node) textCreatorList
				.item(0)).getNodeValue().trim());
		// System.out.println("Creator : " +
		// ((Node)textCreatorList.item(0)).getNodeValue().trim());
		event.setCreator(creator);

		// ------

		// ----

		NodeList startTimeList = suggestionElement
				.getElementsByTagName("StartTime");
		Element startTimeElement = (Element) startTimeList.item(0);

		NodeList textStartTimeList = startTimeElement
				.getChildNodes();
		// System.out.println("StartTime : " + ((Node)
		// textStartTimeList.item(0)).getNodeValue().trim());
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-mm-dd hh:mm:ss");
		Date date = sdf.parse(((Node) textStartTimeList.item(0))
				.getNodeValue().trim());

		event.setStartTimeDate(date);

		// ------

		// ----
		NodeList startLatitudeList = suggestionElement
				.getElementsByTagName("StartLatitude");
		Element startLatitudeElement = (Element) startLatitudeList
				.item(0);

		NodeList textStartLatitudeList = startLatitudeElement
				.getChildNodes();
		float lat = Float.parseFloat(((Node) textStartLatitudeList
				.item(0)).getNodeValue().trim());
		// System.out.println("StartTime : " +
		// ((Node)textStartLatitudeList.item(0)).getNodeValue().trim());
		event.setStartLat(lat);
		// ------

		// ----
		NodeList startLongitudeList = suggestionElement
				.getElementsByTagName("StartLongitude");
		Element startLongitudeElement = (Element) startLongitudeList
				.item(0);

		NodeList textStartLongitudeList = startLongitudeElement
				.getChildNodes();
		float log = Float.parseFloat(((Node) textStartLongitudeList
				.item(0)).getNodeValue().trim());
		// System.out.println("Longitude : " +
		// ((Node)textStartLongitudeList.item(0)).getNodeValue().trim());
		event.setStartLog(log);
		// ------

		// ----
		NodeList statusList = suggestionElement
				.getElementsByTagName("Status");
		Element statusElement = (Element) statusList.item(0);

		NodeList textStatusList = statusElement.getChildNodes();

		boolean status = ((Node) textStatusList.item(0))
				.getNodeValue().trim().equals("1") ? true : false;
		// System.out.println("Status : " +
		// ((Node)textStatusList.item(0)).getNodeValue().trim());
		event.setStatus(status);
		// ------
		return event;
	}

	
	String getContentURL() {
		String urlString = "";

		
		if (this.curLatitudeString == null
				|| this.curLongitudeString == null) {
			urlString = SERVER_URL + "&id="
					+ CarpoActivity.FACEBOOK_USER_ID + "&token="
					+ CarpoActivity.FACEBOOK_TOKEN;

		} else {

			urlString = SERVER_URL + "&id="
					+ CarpoActivity.FACEBOOK_USER_ID + "&token="
					+ CarpoActivity.FACEBOOK_TOKEN + "&lat="
					+ this.curLatitudeString + "&long="
					+ this.curLongitudeString;

		}
		return urlString;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position).toString();
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG);
		Log.d(TAG, "clicked");
	}
	
	class EventListAdaptor extends ArrayAdapter<Event> {

		private ArrayList<Event> items;

		public EventListAdaptor(Context context, int textViewResourceId,
				ArrayList<Event> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.eventlistrow, null);
			}
			Event o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView mt = (TextView) v.findViewById(R.id.middletext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				ImageView imageView = (ImageView) v
						.findViewById(R.id.event_type_icon);
				if (tt != null) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-mm-dd hh:mm:ss");
					tt.setText("Time: " + sdf.format(o.getStartTimeDate()));
				}
				if (mt != null) {
					mt.setText("lat: " + o.getStartLat());
				}
				if (bt != null) {
					bt.setText("log: " + o.getStartLog());
				}
				if (imageView != null) {
					if (o.getClass() == Offer.class) {
						imageView.setImageResource(R.drawable.ic_launcher);
					} else {
						imageView.setImageResource(R.drawable.facebook_icon);
					}

				}

			}
			return v;
		}
	}
}
