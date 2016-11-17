package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class EventLocation {

	public EventLocation() {
	}

	@SerializedName("latitude")
	double latitude;

	@SerializedName("longitude")
	double longitude;

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public EventLocation(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
	}
}