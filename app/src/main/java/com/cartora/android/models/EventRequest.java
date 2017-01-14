package com.cartora.android.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventRequest {

	@SerializedName("name")
	public String name;

	@SerializedName("start_time")
	public long startTime;

	@SerializedName("end_time")
	public long endTime;

	@SerializedName("location")
	public EventLocation location;

	@SerializedName("participants")
	public ArrayList<Integer> participants;

	public EventRequest(String name, long startTime, double lat, double lng, ArrayList<Integer> participants) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = 999999999999999999L; // TODO: Add end time to event request
		this.location = new EventLocation(lat, lng);
		this.participants = participants;
	}
}
