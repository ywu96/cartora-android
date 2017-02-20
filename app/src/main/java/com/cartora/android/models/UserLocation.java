package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

public class UserLocation {

	@SerializedName("id")
	public int userId;

	// TODO: Change this name to "location"
	@SerializedName("locations")
	public LocationLatLng location;

	public static UserLocation from(int userId, LocationLatLng location) {
		return new UserLocation(userId, location);
	}

	private UserLocation(int userId, LocationLatLng location) {
		this.userId = userId;
		this.location = location;
	}
}
