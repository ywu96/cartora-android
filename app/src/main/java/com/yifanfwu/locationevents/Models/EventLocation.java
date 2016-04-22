package com.yifanfwu.locationevents.Models;

import com.google.gson.annotations.SerializedName;

public class EventLocation {

	@SerializedName("latitude")
	protected double latitude;

	@SerializedName("longitude")
	protected double longitude;

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