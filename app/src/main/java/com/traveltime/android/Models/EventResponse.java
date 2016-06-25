package com.traveltime.android.Models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class EventResponse {

	public EventResponse(){}

	@SerializedName("_id")
	String Id;

	@SerializedName("eventName")
	String eventName;

	@SerializedName("timeSecs")
	long timeSecs;

	@SerializedName("__v")
	int V;

	@SerializedName("eventLocation")
	EventLocation eventLocation;

	@SerializedName("users")
	List<EventUserResponse> eventUserResponses = new ArrayList<>();

	public String getId() {
		return this.Id;
	}

	public String getEventName() {
		return this.eventName;
	}

	public long getTimeSecs() {
		return this.timeSecs;
	}

	public int getV() {
		return this.V;
	}

	public EventLocation getEventLocation() {
		return this.eventLocation;
	}

	public List<EventUserResponse> getEventUserResponses() {
		return this.eventUserResponses;
	}

	public EventResponse(EventResponse eventResponse) {
		this.Id = eventResponse.getId();
		this.eventName = eventResponse.getEventName();
		this.timeSecs = eventResponse.getTimeSecs();
		this.V = eventResponse.getV();
		this.eventLocation = eventResponse.getEventLocation();
		this.eventUserResponses = eventResponse.getEventUserResponses();
	}
}