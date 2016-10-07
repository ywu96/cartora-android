package com.traveltime.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class EventResponse {

	public EventResponse() {
	}

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
		return Id;
	}

	public String getEventName() {
		return eventName;
	}

	public long getTimeSecs() {
		return timeSecs;
	}

	public int getV() {
		return V;
	}

	public EventLocation getEventLocation() {
		return eventLocation;
	}

	public List<EventUserResponse> getEventUserResponses() {
		return eventUserResponses;
	}

	public EventResponse(EventResponse eventResponse) {
		Id = eventResponse.getId();
		eventName = eventResponse.getEventName();
		timeSecs = eventResponse.getTimeSecs();
		V = eventResponse.getV();
		eventLocation = eventResponse.getEventLocation();
		eventUserResponses = eventResponse.getEventUserResponses();
	}
}