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
	public LocationLatLng location;

	@SerializedName("participants")
	public ArrayList<Participant> participants;

	public EventRequest(String name, long startTime, double lat, double lng, ArrayList<Participant> participants) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = startTime + 5L; // TODO: Add end time to event request
		this.location = LocationLatLng.from(lat, lng);
		this.participants = participants;
	}

	public static class Participant {
		@SerializedName("id")
		public int userId;

		public static Participant from(int userId) {
			return new Participant(userId);
		}

		private Participant(int userId) {
			this.userId = userId;
		}
	}
}
