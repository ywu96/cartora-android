package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

public class LocationUpdateRequest {

	@SerializedName("userId")
	private String userId;

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("longitude")
	private double longitude;

	public LocationUpdateRequest(String userId, double latitude, double longitude) {
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}