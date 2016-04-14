package com.yifanfwu.locationevents.Rest;

import android.util.Log;

import com.yifanfwu.locationevents.Models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestServer {
    public static final String LOG_TAG = "RestServer";

    public static final String BASE_URL = "http://220f59ea.ngrok.io/";
    protected static RestServer restServer = null;

    protected RestService restService;

//    protected Retrofit retrofit;

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

    public void getEvents() {
        Call<List<Event>> eventListCall = this.restService.getEvents();
        eventListCall.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    for (Event event : response.body()) {
                        Log.d(LOG_TAG, "id: " + event.Id);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }


}
