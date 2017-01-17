package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class EventLocation {

	public EventLocation() {
	}

	@SerializedName("lat")
	public double latitude;

	@SerializedName("lng")
	public double longitude;

	public EventLocation(double lat, double lng) {
		latitude = lat;
		longitude = lng;
	}
}