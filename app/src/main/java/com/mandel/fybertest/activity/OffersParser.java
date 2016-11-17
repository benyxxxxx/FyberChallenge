package com.mandel.fybertest.activity;

import com.mandel.fybertest.model.Offer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss on 11/6/2016.
 */
public class OffersParser {
	
	public static List<Offer> parseJson(String data) {
		ArrayList<Offer> list = new ArrayList<Offer>();
		
		try {
			JSONObject obj = new JSONObject(data);
			JSONArray offers = obj.getJSONArray("offers");

			for (int i = 0; i < offers.length(); i++) {
				JSONObject offer = (JSONObject)offers.get(i);
				
				Offer item = new Offer();
				item.setTitle(offer.getString("title"));
				item.setTeaser(offer.getString("teaser"));
				item.setThumbnailHires(offer.getJSONObject("thumbnail").getString("hires"));
				item.setPayout(offer.getInt("payout"));

				list.add(item);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static List<Offer> parseXml(String data) {
		
		ArrayList<Offer> list = new ArrayList<Offer>();	
		XmlPullParserFactory factory = null;
		try {


			factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(data));
			
			String ItemName = "";
			int eventType = parser.getEventType();
			Offer newItem = null;
			String start;
			String end;
			boolean inside = false;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					
				case XmlPullParser.START_TAG:
			
					start = parser.getName();
					
					if (start.equals("offer")) {
						inside = true;
						newItem = new Offer();
					}
					if (inside == true) {
						if (start.equals("title")) {
							newItem.setTitle(parser.nextText());
						}
						if (start.equals("teaser")) {
							newItem.setTeaser(parser.nextText());
						}
						if (start.equals("hires")) {
							newItem.setThumbnailHires(parser.nextText());
						}
						if (start.equals("payout")) {
							newItem.setPayout(Integer.valueOf(parser.nextText()));
						}
					}
					
					break;
					
				case XmlPullParser.END_TAG:
					end = parser.getName();
					if (end.equals("offer")) {
						inside = false;
						list.add(newItem);
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
