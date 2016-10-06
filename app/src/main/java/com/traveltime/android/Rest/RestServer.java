package com.traveltime.android.rest;

import com.traveltime.android.models.EventRequest;
import com.traveltime.android.models.EventResponse;
import com.traveltime.android.models.LocationUpdateRequest;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;


public class RestServer {
	public static final String LOG_TAG = "RestServer";

	// TODO: remember to update
	public static final String BASE_URL = "https://quiet-bayou-32241.herokuapp.com/";
	protected static RestServer restServer = null;

	protected RestService restService;

	public static RestServer getInstance() {
		if (restServer == null) {
			restServer = new RestServer();
		}
		return restServer;
	}

	public RestServer() {
		Retrofit retrofit = new Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(BASE_URL)
				.build();
		restService = retrofit.create(RestService.class);
	}

	//Callback for result
	public interface Callback<E> {
		void result(E result);
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
