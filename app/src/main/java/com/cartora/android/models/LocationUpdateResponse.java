package com.cartora.android.models;

import java.util.ArrayList;

public class LocationUpdateResponse {

	public ArrayList<UserLocation> locations;

	public LocationUpdateResponse(ArrayList<UserLocation> locations) {
		this.locations = locations;
	}
}
