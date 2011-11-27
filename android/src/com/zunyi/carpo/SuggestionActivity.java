package com.zunyi.carpo;

import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.zunyi.carpo.model.Event;
import com.zunyi.carpo.model.Offer;
import com.zunyi.carpo.model.Request;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestionActivity extends ListActivity {

	private static final String TAG = "SUGGESTION_ACTIVITY";
	private static final String SERVER_URL_SUGGESTIONS = "http://70.64.6.83:8080/cmpt412_project/suggestions?";
	private static final int suggestionCount = 8;
	private ProgressDialog mSpinner;
	ArrayList<Event> events = null;
	private EventListAdaptor eventListAdaptor = null;
	private Runnable viewSuggestions;
	Button skipButton = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.suggestion);

		events = new ArrayList<Event>();
		eventListAdaptor = new EventListAdaptor(this, R.layout.offerlistrow,
				events);
		setListAdapter(eventListAdaptor);

		skipButton = (Button) findViewById(R.id.skipButton);
		skipButton.setOnClickListener(new OnSkipButtonPressedListner());

		mSpinner = new ProgressDialog(this);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading Suggestions...");

		viewSuggestions = new Runnable() {
			@Override
			public void run() {
				getSuggestions();
			}
		};

		Thread thread = new Thread(null, viewSuggestions, "MagentoBackground");
		thread.start();
		mSpinner.show();
	}

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			System.out.println("events.size()" + events.size());
			if (events != null && events.size() > 0) {
				eventListAdaptor.notifyDataSetChanged();
				for (int i = 0; i < events.size(); i++) {
					eventListAdaptor.add(events.get(i));
				}
			}
			mSpinner.dismiss();
			eventListAdaptor.notifyDataSetChanged();
		}
	};

	private void getSuggestions() {

		try {
			events = new ArrayList<Event>();
			
			URL url = new URL(SERVER_URL_SUGGESTIONS + "id="
					+ CarpoActivity.FACEBOOK_USER_ID + "&token="
					+ CarpoActivity.FACEBOOK_TOKEN + "&count="
					+ suggestionCount);

			Log.d(TAG, url.toString());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();

			NodeList suggestionList = doc.getElementsByTagName("Suggestion");
			int totalSuggestion = suggestionList.getLength();
			System.out.println("Total no of suggestions : " + totalSuggestion);

			for (int s = 0; s < totalSuggestion; s++) {

				Node suggestionNode = suggestionList.item(s);
				if (suggestionNode.getNodeType() == Node.ELEMENT_NODE) {

					Event event;
					Element suggestionElement = (Element) suggestionNode;

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
					//System.out.println("StartTime : " + ((Node) textStartTimeList.item(0)).getNodeValue().trim());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-mm-dd hh:mm:ss");
					Date date = sdf.parse(((Node) textStartTimeList.item(0)).getNodeValue()
							.trim());

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

					events.add(event);

				}// end of if clause

			}// end of for loop with s var

		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

		runOnUiThread(returnRes);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position).toString();
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG);
		Log.d(TAG,"clicked");
	}

	private class OnSkipButtonPressedListner implements OnClickListener {

		@Override
		public void onClick(View v) {

			Intent myIntent = new Intent(SuggestionActivity.this,
					MainMenuActivity.class);
			SuggestionActivity.this.startActivity(myIntent);
			finish();
		}

	}

	private class EventListAdaptor extends ArrayAdapter<Event> {

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
				v = vi.inflate(R.layout.offerlistrow, null);
			}
			Event o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView mt = (TextView) v.findViewById(R.id.middletext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				ImageView imageView = (ImageView)v.findViewById(R.id.event_type_icon);
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
					}
					else
					{
						imageView.setImageResource(R.drawable.facebook_icon);
					}
					
					
				}
				
				
			}
			return v;
		}
	}

}
