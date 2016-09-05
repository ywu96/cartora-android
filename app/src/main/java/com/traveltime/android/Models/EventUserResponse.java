package com.traveltime.android.Models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class EventUserResponse {

	public EventUserResponse(){}

	@SerializedName("userId")
	String userId;

	@SerializedName("latitude")
	double latitude;

	@SerializedName("longitude")
	double longitude;

	@SerializedName("_id")
	String Id;

	public String getUserId() {
		return userId;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getId() {
		return Id;
	}
}