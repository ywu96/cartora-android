package com.traveltime.android.Rest;

import com.traveltime.android.Models.EventRequest;
import com.traveltime.android.Models.EventResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {

	@GET("events/user/{id}")
	Call<ArrayList<EventResponse>> getEvents(@Path("id") String uid);

	@POST("events")
	Call<EventResponse> createEvent(@Body EventRequest eventRequest);

	@DELETE("events/{id}")
	Call<EventResponse> deleteEvent(@Path("id") String eventId);
}
