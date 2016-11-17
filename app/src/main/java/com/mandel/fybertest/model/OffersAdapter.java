package com.mandel.fybertest.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.app.Activity;
import com.bumptech.glide.Glide;

import com.mandel.fybertest.R;
import com.mandel.fybertest.model.Offer;

import java.util.List;

public class OffersAdapter extends BaseAdapter {

	private final Activity mContext;
	List<Offer> mItems;
	
	public OffersAdapter(Activity context) {
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		return mItems.size();
	}
	
	@Override
	public Offer getItem(int position) {
		return mItems.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public  void setItems(List<Offer> items)
	{
		this.mItems = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
		  
			LayoutInflater inflator = mContext.getLayoutInflater();
			view = inflator.inflate(R.layout.item_offer1, null);
		}
		
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(getItem(position).getTitle());
		
		TextView teaser = (TextView) view.findViewById(R.id.teaser);
		teaser.setText(getItem(position).getTeaser());
		
		TextView payout = (TextView) view.findViewById(R.id.payout);
		payout.setText(getItem(position).getPayout());
		
		ImageView imageView = (ImageView) view.findViewById(R.id.img);
		Glide.with(imageView.getContext()).load(getItem(position).getThumbnailHires()).into(imageView);
		return view;
		
	}

}
