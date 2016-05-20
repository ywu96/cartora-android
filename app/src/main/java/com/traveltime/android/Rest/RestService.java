package com.traveltime.android.Rest;

import com.traveltime.android.Models.EventRequest;
import com.traveltime.android.Models.EventResponse;
import com.traveltime.android.Models.LocationUpdateRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestService {

	@GET("events/user/{eventId}")
	Call<ArrayList<EventResponse>> getEvents(@Path("eventId") String uid);

	@POST("events")
	Call<EventResponse> createEvent(@Body EventRequest eventRequest);

	@DELETE("events/{eventId}")
	Call<EventResponse> deleteEvent(@Path("eventId") String eventId);

	@PUT("events/{eventId}/location")
	Call<EventResponse> updateLocation(@Path("eventId") String eventId, @Body LocationUpdateRequest locationUpdateRequest);
}
