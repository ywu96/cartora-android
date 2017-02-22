package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class EventResponse {

	// Empty constructor for Parceler
	public EventResponse() {
	}

	@SerializedName("id")
	public String id;

	@SerializedName("name")
	public String name;

	@SerializedName("start_time")
	public long startTime;

	@SerializedName("end_time")
	public long endTime;

	@SerializedName("location")
	public LocationLatLng location;
}