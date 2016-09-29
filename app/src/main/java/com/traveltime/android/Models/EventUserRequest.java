package com.traveltime.android.models;

import com.google.gson.annotations.SerializedName;

public class EventUserRequest {

	@SerializedName("userId")
	protected String userId;

	@SerializedName("latitude")
	protected double latitude;

	@SerializedName("longitude")
	protected double longitude;

	public EventUserRequest(String userId) {
		this.userId = userId;
	}
}
