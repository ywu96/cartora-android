package com.cartora.android.rest;

import com.cartora.android.models.EventRequest;
import com.cartora.android.models.EventResponse;
import com.cartora.android.models.EventWithParticipantsResponse;
import com.cartora.android.models.FcmIdUpdateRequest;
import com.cartora.android.models.LocationLatLng;
import com.cartora.android.models.SignUpRequest;
import com.cartora.android.models.UserResponse;
import com.cartora.android.utils.Strings;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class RestServer {

	// TODO: remember to update
	private static final String BASE_URL = "https://cartora.herokuapp.com/";
	private static RestServer restServer = null;

	private static RestService restService;
	private static OkHttpClient.Builder clientBuilder;
	private static Retrofit.Builder retrofitBuilder;

	private RestServer() {
		clientBuilder = new OkHttpClient.Builder();
		retrofitBuilder = new Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(BASE_URL);
	}

	public static RestServer createService() {
		return createService(null);
	}

	public static RestServer createService(final String authToken) {
		if (restServer == null) {
			restServer = new RestServer();
		}

		clientBuilder.addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request original = chain.request();

				Request.Builder requestBuilder = original.newBuilder()
						.header("Content-Type", "application/json")
						.header("Accept", "application/vnd.traveltime.v1")
						.method(original.method(), original.body());

				if (!Strings.isNullOrEmpty(authToken)) {
					requestBuilder.header("Authorization", authToken);
				}

				Request request = requestBuilder.build();
				return chain.proceed(request);
			}
		});
		OkHttpClient client = clientBuilder.build();
		Retrofit retrofit = retrofitBuilder.client(client).build();
		restService = retrofit.create(RestService.class);

		return restServer;
	}

	public Observable<UserResponse> signUp(SignUpRequest signUpRequest) {
		return restService.signUp(signUpRequest);
	}

	public Observable<UserResponse> updateFcmId(FcmIdUpdateRequest fcmIdUpdateRequest) {
		return restService.updateFcmId(fcmIdUpdateRequest);
	}

	public Observable<ArrayList<EventResponse>> getEvents(int userId) {
		return restService.getEvents(userId);
	}

	public Observable<EventWithParticipantsResponse> createEvent(int userId, EventRequest eventRequest) {
		return restService.createEvent(userId, eventRequest);
	}

	public Observable<EventResponse> deleteEvent(int userId, int eventId) {
		return restService.deleteEvent(userId, eventId);
	}

	public Observable<LocationLatLng> updateLocationBackground(int userId, LocationLatLng locationLatLng) {
		return restService.updateLocationBackground(userId, locationLatLng);
	}

	public Observable<EventResponse> updateLocation(int userId, int eventId, LocationLatLng locationLatLng) {
		return restService.updateLocation(userId, eventId, locationLatLng);
	}
}
