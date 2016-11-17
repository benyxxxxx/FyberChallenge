package com.mandel.fybertest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;

import com.mandel.fybertest.model.Offer;
import com.mandel.fybertest.model.OffersAdapter;

import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		setTitle("Offers");

		Intent intent = getIntent();
		String format = intent.getStringExtra("format");
		String data = intent.getStringExtra("data");

		if (format != null && data != null) {
			OffersAdapter offersAdapter = new OffersAdapter(this);
			offersAdapter.setItems(parserData(format, data));
			setListAdapter(offersAdapter);
		}
	}
	
	private List<Offer> parserData(String format, String data) {
		List<Offer> offers = new ArrayList<>();
		
		if(format.equals("xml")){
			offers = OffersParser.parseXml(data);
		} else if(format.equals("json")){
			offers = OffersParser.parseJson(data);
		}
		return offers;
	}	
}
