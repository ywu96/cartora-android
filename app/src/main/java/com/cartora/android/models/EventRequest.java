package com.cartora.android.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventRequest {

	@SerializedName("eventName")
	protected String eventName;

	@SerializedName("eventLocation")
	protected EventLocation eventLocation;

	@SerializedName("users")
	protected ArrayList<EventUserRequest> users;

	@SerializedName("timeSecs")
	protected long timeSecs;

	public EventRequest(String eventName, ArrayList<EventUserRequest> users, double lat, double lng, long time) {
		this.eventName = eventName;
		this.users = users;
		this.eventLocation = new EventLocation(lat, lng);
		this.timeSecs = time;
	}
}
