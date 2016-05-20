package com.traveltime.android.Models;

import com.google.gson.annotations.SerializedName;

public class EventUserResponse {

	@SerializedName("userId")
	protected String userId;

	@SerializedName("latitude")
	protected double latitude;

	@SerializedName("longitude")
	protected double longitude;

	@SerializedName("_id")
	protected String Id;

	public String getUserId() {
		return this.userId;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public String getId() {
		return this.Id;
	}
}