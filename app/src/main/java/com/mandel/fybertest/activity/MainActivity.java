package com.mandel.fybertest.activity;

import android.app.Activity;
import java.util.Map;
import java.util.HashMap;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.content.Intent;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import android.widget.AdapterView.OnItemClickListener;

import com.mandel.fybertest.model.Param;
import com.mandel.fybertest.model.Params;
import com.mandel.fybertest.R;
import java.net.HttpURLConnection;

public class MainActivity extends Activity {
	
	private ConnectionUtil mConnectionUtil = null;
	private Params mParamModel;
	
	final String FYBER_URL = "http://api.fyber.com/feed/v1/offers.json";
	final String RESPONSE_SIGNATURE = "X-Sponsorpay-Response-Signature";
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mParamModel = new Params() {
				public String getParamValue(int id) {
					TextView text = (TextView)findViewById(id);
					return text.getText().toString();
				}	
			};
		
		mParamModel.put(R.id.appid,"2070", "appid");
		mParamModel.put(R.id.format,"json", "format");
		mParamModel.put(R.id.uid,"spiderman", "uid");
		mParamModel.put(R.id.offer_types,"112", "offer_types");
		mParamModel.put(R.id.ip, "109.235.143.113", "ip");
		mParamModel.put(R.id.locale,"DE", "locale");
		mParamModel.putApiKey(R.id.apikey,"1c915e3b5d42d05136185030892fbb846c278927");
		
		
		mConnectionUtil = new 	ConnectionUtil(getApplicationContext());
		setContentView(R.layout.activity_main);
		
		for (Param entry : mParamModel) {
			TextView text = (TextView)findViewById(entry.getId());
			text.setText(entry.getDefaultValue());
		}
		
		Button button = (Button)findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mConnectionUtil.execute(FYBER_URL, RESPONSE_SIGNATURE, mParamModel, mHandler);
				}
			});
		
	}

		
	Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				Bundle bundle = message.getData();
				
				String msg = bundle.getString("msg");
				String result = bundle.getString("result");
				String sign = bundle.getString("sign");
				
				if (bundle.getInt("code") == HttpURLConnection.HTTP_OK) {
					
					if (mParamModel.getSignature(result).equals(sign)) {
						
						onResult(msg, result);
						
					} else {
						alert("Error", "Signature doesn't match.");
					}
				}
				else {
					alert("Error", bundle.getInt("code") + " : " + message);
				}
			}
		};
	
	
	private void onResult(String message, String data) {

		if (message.equals("OK")) {
			Intent intent = new Intent(this, OffersActivity.class);
			intent.putExtra("data",data);
			intent.putExtra("format", mParamModel.getValue("format"));
			startActivity(intent);

		} else {
			alert("hmmm", "No offers");
		}
	}
	
	private void alert(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialogBuilder.create()
			.show();
	}
}
