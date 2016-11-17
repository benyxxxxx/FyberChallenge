package com.mandel.fybertest.model;

/**
 * Created by ss on 11/6/2016.
 */
public class Offer {

	private String mTitle;
	private String mTeaser;
	private String mThumbnailHires;
	private int mPayout;

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getTeaser() {
		return mTeaser;
	}

	public void setTeaser(String teaser) {
		this.mTeaser = teaser;
	}

	public String getThumbnailHires() {
		return mThumbnailHires;
	}

	public void setThumbnailHires(String thumbnailHires) {
		this.mThumbnailHires = thumbnailHires;
	}

	public String getPayout() {
		return "Payout is " + mPayout;
	}

	public void setPayout(int payout) {
		this.mPayout = payout;
	}
}
