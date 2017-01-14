package com.cartora.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventWithParticipantsResponse {

	@SerializedName("id")
	public int id;

	@SerializedName("name")
	public String name;

	@SerializedName("start_time")
	public long startTime;

	@SerializedName("end_time")
	public long endTime;

	@SerializedName("location")
	public EventLocation location;

	@SerializedName("host")
	public UserPublicResponse host;

	@SerializedName("participants")
	public ArrayList<UserPublicResponse> participants;
}
