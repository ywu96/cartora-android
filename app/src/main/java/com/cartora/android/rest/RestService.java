package com.cartora.android.rest;

import com.cartora.android.models.EventRequest;
import com.cartora.android.models.EventResponse;
import com.cartora.android.models.EventWithParticipantsResponse;
import com.cartora.android.models.FcmIdUpdateRequest;
import com.cartora.android.models.LocationLatLng;
import com.cartora.android.models.SignUpRequest;
import com.cartora.android.models.UserResponse;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RestService {

	@POST("users")
	Observable<UserResponse> signUp(@Body SignUpRequest signUpRequest);

	@PUT("users/{userId}")
	Observable<UserResponse> updateFcmId(@Body FcmIdUpdateRequest fcmIdUpdateRequest);

	@GET("users/{userId}/events")
	Observable<ArrayList<EventResponse>> getEvents(@Path("userId") int userId);

	@POST("users/{userId}/events")
	Observable<EventWithParticipantsResponse> createEvent(@Path("userId") int userId, @Body EventRequest eventRequest);

	@DELETE("/users/{userId}/events/{eventId}")
	Observable<EventResponse> deleteEvent(@Path("userId") int userId, @Path("eventId") int eventId);

	@PUT("users/{userId}/location")
	Observable<EventResponse> updateLocationBackground(@Path("userId") int userId, @Body LocationLatLng locationLatLng);

	@PUT("users/{userId}/events/{eventId}/location")
	Observable<EventResponse> updateLocation(@Path("userId") int userId, @Path("eventId") int eventId, @Body LocationLatLng locationLatLng);
}
