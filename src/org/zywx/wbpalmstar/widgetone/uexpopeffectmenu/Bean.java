package org.zywx.wbpalmstar.widgetone.uexpopeffectmenu;

import android.os.Parcel;
import android.os.Parcelable;

public class Bean implements Parcelable {
	private String text;
	private String url;
	private String textColor;
	public Bean() {
		super();
	}
	public Bean(String text, String url, String textColor) {
		super();
		this.text = text;
		this.url = url;
		this.textColor = textColor;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTextColor() {
		return textColor;
	}
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}
	
}
