package com.traveltime.android.rest;

import com.traveltime.android.models.EventRequest;
import com.traveltime.android.models.EventResponse;
import com.traveltime.android.models.LocationUpdateRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

//	public void getEvents(String uid, final Callback<ArrayList<EventResponse>> callback) {
//		restService.getEvents(uid);
//		call.enqueue(new RetrofitCall.RetrofitCallback<ArrayList<EventResponse>>() {
//			@Override
//			public void onResponse(Call<ArrayList<EventResponse>> call, Response<ArrayList<EventResponse>> response) {
//				if (response.isSuccessful()) {
//					callback.result(response.body());
//				}
//			}
//
//			@Override
//			public void onFailure(Call<ArrayList<EventResponse>> call, Throwable t) {
//				t.printStackTrace();
//			}
//		});
//	}

	public void createEvent(EventRequest eventRequest, final Callback<EventResponse> callback) {
		Call<EventResponse> call = this.restService.createEvent(eventRequest);
		call.enqueue(new RetrofitCall.RetrofitCallback<EventResponse>() {
			@Override
			public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
				if (response.isSuccessful()) {
					callback.result(response.body());
				}
			}

			@Override
			public void onFailure(Call<EventResponse> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void deleteEvent(String eventId, final Callback<EventResponse> callback) {
		Call<EventResponse> call = restService.deleteEvent(eventId);
		call.enqueue(new RetrofitCall.RetrofitCallback<EventResponse>() {
			@Override
			public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
				if (response.isSuccessful()) {
					callback.result(response.body());
				}
			}

			@Override
			public void onFailure(Call<EventResponse> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void updateLocation(String eventId, LocationUpdateRequest locationUpdateRequest, final Callback<EventResponse> callback) {
		Call<EventResponse> call = restService.updateLocation(eventId, locationUpdateRequest);
		call.enqueue(new RetrofitCall.RetrofitCallback<EventResponse>() {
			@Override
			public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
				if (response.isSuccessful()) {
					callback.result(response.body());
				}
			}

			@Override
			public void onFailure(Call<EventResponse> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}

}
