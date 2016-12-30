package com.cartora.android.rest;

import com.cartora.android.models.EventRequest;
import com.cartora.android.models.EventResponse;
import com.cartora.android.models.LocationUpdateRequest;
import com.cartora.android.models.SignUpRequest;
import com.cartora.android.models.UserResponse;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RestService {

	@Headers({
			"Content-Type: application/json",
			"Accept: application/vnd.traveltime.v1"
	})
	@POST("users")
	Observable<UserResponse> signUp(@Body SignUpRequest signUpRequest);

	@GET("events/user/{eventId}")
	Observable<ArrayList<EventResponse>> getEvents(@Path("eventId") String uid);

	@POST("events")
	Observable<EventResponse> createEvent(@Body EventRequest eventRequest);

	@DELETE("events/{eventId}")
	Observable<EventResponse> deleteEvent(@Path("eventId") String eventId);

	@PUT("events/{eventId}/location")
	Observable<EventResponse> updateLocation(@Path("eventId") String eventId, @Body LocationUpdateRequest locationUpdateRequest);
}