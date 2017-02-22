package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class LocationLatLng {

	// Empty constructor for Parceler
	public LocationLatLng() {}

	@SerializedName("lat")
	public double latitude;

	@SerializedName("lng")
	public double longitude;

	public static LocationLatLng from(double lat, double lng) {
		return new LocationLatLng(lat, lng);
	}

	private LocationLatLng(double lat, double lng) {
		latitude = lat;
		longitude = lng;
	}
}