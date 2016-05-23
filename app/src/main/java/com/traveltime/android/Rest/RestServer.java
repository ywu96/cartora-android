package com.traveltime.android.Rest;

import com.traveltime.android.Models.EventRequest;
import com.traveltime.android.Models.EventResponse;
import com.traveltime.android.Models.LocationUpdateRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		this.restService = retrofit.create(RestService.class);
	}

	//Callback for result
	public interface Callback<E> {
		void result(E result);
	}

	public void getEvents(String uid, final Callback<ArrayList<EventResponse>> callback) {
		Call<ArrayList<EventResponse>> call = this.restService.getEvents(uid);
		call.enqueue(new RetrofitCall.RetrofitCallback<ArrayList<EventResponse>>() {
			@Override
			public void onResponse(Call<ArrayList<EventResponse>> call, Response<ArrayList<EventResponse>> response) {
				if (response.isSuccessful()) {
					callback.result(response.body());
				}
			}

			@Override
			public void onFailure(Call<ArrayList<EventResponse>> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}

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
		Call<EventResponse> call = this.restService.deleteEvent(eventId);
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
		Call<EventResponse> call = this.restService.updateLocation(eventId, locationUpdateRequest);
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