package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class EventLocation {

	public EventLocation() {
	}

	@SerializedName("lat")
	public double latitude;

	@SerializedName("lon")
	public double longitude;

	public EventLocation(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}
}