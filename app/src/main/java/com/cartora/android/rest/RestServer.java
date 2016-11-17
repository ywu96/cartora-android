package com.cartora.android.rest;

import com.cartora.android.models.EventRequest;
import com.cartora.android.models.EventResponse;
import com.cartora.android.models.LocationUpdateRequest;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;


public class RestServer {

	// TODO: remember to update
	private static final String BASE_URL = "https://quiet-bayou-32241.herokuapp.com/";
	private static RestServer restServer = null;

	private RestService restService;

	public static RestServer getInstance() {
		if (restServer == null) {
			restServer = new RestServer();
		}
		return restServer;
	}

	private RestServer() {
		Retrofit retrofit = new Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(BASE_URL)
				.build();
		restService = retrofit.create(RestService.class);
	}

	public Observable<ArrayList<EventResponse>> getEvents(String uid) {
		return restService.getEvents(uid);
	}

	public Observable<EventResponse> createEvent(EventRequest eventRequest) {
		return restService.createEvent(eventRequest);
	}

	public Observable<EventResponse> deleteEvent(String eventId) {
		return restService.deleteEvent(eventId);
	}

	public Observable<EventResponse> updateLocation(String eventId, LocationUpdateRequest locationUpdateRequest) {
		return restService.updateLocation(eventId, locationUpdateRequest);
	}

}
