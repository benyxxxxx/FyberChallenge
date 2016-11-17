package com.mandel.fybertest.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.mandel.fybertest.model.Param;
import com.mandel.fybertest.model.Params;

public class ConnectionUtil {

	private Context mContext = null;
	
	public ConnectionUtil(Context context) {
		this.mContext = context;	
	}
	
	public void execute(String URL, String reponseSign, Params params, Handler handler) {
		new Extractor(URL, reponseSign, handler,  params).execute("");
	}
	
	class Extractor extends AsyncTask<String, Void, Message> {

		private Params mParams = null;
		private Handler mHandler = null;
		private String mUrl;
		private String mReponseSign;
		
		public Extractor(String URL, String reponseSign, Handler handler, Params params) {
			mHandler = handler;
			mParams = params;
			mUrl = URL;
			mReponseSign = reponseSign;
		}

		
		@Override
		protected Message doInBackground(String... params) {

			String result = "";
			String sign = "";
			String msg = "";

			getExtraParams();
			String paramsStr = mParams.buildResult(); 
			
			String theUrl = mUrl + "?" + paramsStr;
			int delay = 5000; 
			int resCode = HttpURLConnection.HTTP_NOT_FOUND;

			try {
				URL url = new URL(theUrl);
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				if (conn != null) {
					conn.setConnectTimeout(delay);
					conn.setUseCaches(false);
					conn.setRequestProperty("Connection", "Keep-Alive"); 
					
					resCode = conn.getResponseCode();
					msg = conn.getResponseMessage();
					sign = conn.getHeaderField(mReponseSign);
					
					if (resCode == HttpURLConnection.HTTP_OK) {
						BufferedReader reader =
							new BufferedReader(
									   new InputStreamReader(conn.getInputStream()));
						StringBuilder buffer = new StringBuilder();
						
						String read;
						
						while (( read = reader.readLine()) != null) {
							buffer.append(read);
						}
						reader.close();
						result = buffer.toString();
						
					} 
					conn.disconnect();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Message message = Message.obtain();
			Bundle bundle = new Bundle();

			bundle.putInt("code", resCode);
			bundle.putString("msg", msg);
			bundle.putString("result", result);
			bundle.putString("sign", sign);
			message.setData(bundle);
			
			return message;
		}

		private void getExtraParams() {
		
			mParams.put(0, Build.VERSION.RELEASE, "os_version");
			String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
			mParams.put(0, timeStamp, "timestamp");
		
			boolean allowTrack = false;
			String googleAdId = "";
			AdvertisingIdClient.Info adInfo;
			try {
				adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
				allowTrack = adInfo.isLimitAdTrackingEnabled();
				if (allowTrack) 
					googleAdId = adInfo.getId();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		
		
		
			mParams.put(0, googleAdId, "google_ad_id");
			mParams.put(0, String.valueOf(allowTrack), "google_ad_id_limited_tracking_enabled");
		
		}
		
		@Override
		protected void onPostExecute(Message msg) {
			mHandler.sendMessage(msg);
		}
	}


	
	
}
