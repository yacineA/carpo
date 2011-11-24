package com.zunyi.carpo;

import java.net.URL;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SuggestionActivity extends ListActivity {

	private static final String TAG = "SUGGESTION_ACTIVITY";
	private static final String SERVER_URL_SUGGESTIONS = "http://70.64.6.83:8080/cmpt412_project/suggestions?";
	private ProgressDialog mSpinner;

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

		skipButton = (Button) findViewById(R.id.skipButton);
		skipButton.setOnClickListener(new OnSkipButtonPressedListner());

		mSpinner = new ProgressDialog(this);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading Suggestions...");

		this.getSuggestions();
	}

	public void getSuggestions() {
		
		try {

			URL url = new URL(SERVER_URL_SUGGESTIONS + "id="
					+ CarpoActivity.FACEBOOK_USER_ID + "&token="
					+ CarpoActivity.FACEBOOK_TOKEN);

			Log.d(TAG, url.toString());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			
			NodeList suggestionList = doc.getElementsByTagName("Suggestion");
			int totalSuggestion = suggestionList.getLength();
			System.out.println("Total no of suggestions : " + totalSuggestion);
			
            for(int s = 0; s < totalSuggestion ; s++){


                Node suggestionNode = suggestionList.item(s);
                if(suggestionNode.getNodeType() == Node.ELEMENT_NODE){


                    Element suggestionElement = (Element)suggestionNode;

                    //-------
                    NodeList typeList = suggestionElement.getElementsByTagName("Type");
                    Element typeElement = (Element)typeList.item(0);

                    NodeList textTypeList = typeElement.getChildNodes();
                    //System.out.println("Type : " + ((Node)textTypeList.item(0)).getNodeValue().trim());

                    //-------
                    NodeList idList = suggestionElement.getElementsByTagName("ID");
                    Element idElement = (Element)idList.item(0);

                    NodeList textIDList = idElement.getChildNodes();
                    //System.out.println("ID : " + ((Node)textIDList.item(0)).getNodeValue().trim());

                    
                    //----
                    NodeList creatorList = suggestionElement.getElementsByTagName("Creator");
                    Element creatorElement = (Element)creatorList.item(0);

                    NodeList textCreatorList = creatorElement.getChildNodes();
                    //System.out.println("Creator : " + ((Node)textCreatorList.item(0)).getNodeValue().trim());
                    
                    //------

                    //----
                    NodeList startTimeList = suggestionElement.getElementsByTagName("StartTime");
                    Element startTimeElement = (Element)startTimeList.item(0);

                    NodeList textStartTimeList = startTimeElement.getChildNodes();
                    //System.out.println("StartTime : " + ((Node)textStartTimeList.item(0)).getNodeValue().trim());
                    
                    //------

                    //----
                    NodeList startLatitudeList = suggestionElement.getElementsByTagName("StartLatitude");
                    Element startLatitudeElement = (Element)startLatitudeList.item(0);

                    NodeList textStartLatitudeList = startLatitudeElement.getChildNodes();
                    //System.out.println("StartTime : " + ((Node)textStartLatitudeList.item(0)).getNodeValue().trim());
                    
                    //------
                    
                    //----
                    NodeList startLongitudeList = suggestionElement.getElementsByTagName("StartLongitude");
                    Element startLongitudeElement = (Element)startLongitudeList.item(0);

                    NodeList textStartLongitudeList = startLongitudeElement.getChildNodes();
                    //System.out.println("Longitude : " + ((Node)textStartLongitudeList.item(0)).getNodeValue().trim());
                    
                    //------
                    
                    //----
                    NodeList statusList = suggestionElement.getElementsByTagName("Status");
                    Element statusElement = (Element)statusList.item(0);

                    NodeList textStatusList = statusElement.getChildNodes();
                    //System.out.println("Status : " + ((Node)textStatusList.item(0)).getNodeValue().trim());
                    
                    //------
                    
                    //----
                    NodeList capacityList = suggestionElement.getElementsByTagName("Capacity");
                    Element capacityElement = (Element)capacityList.item(0);

                    NodeList textCapacityList = capacityElement.getChildNodes();
                    //System.out.println("Capacity : " + ((Node)textCapacityList.item(0)).getNodeValue().trim());
                    
                    //------
                    
                    //----
                    NodeList sharedList = suggestionElement.getElementsByTagName("Shared");
                    Element sharedElement = (Element)sharedList.item(0);

                    NodeList textSharedList = sharedElement.getChildNodes();
                    //System.out.println("Shared : " +  ((Node)textSharedList.item(0)).getNodeValue().trim());
                    
                    //------
                    
                    
                }//end of if clause


            }//end of for loop with s var

		} catch (Exception e) {
			//System.out.println("XML Pasing Excpetion = " + e);
		}

		return;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG);
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

}
