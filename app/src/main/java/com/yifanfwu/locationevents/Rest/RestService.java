package com.yifanfwu.locationevents.Rest;

import com.yifanfwu.locationevents.Models.EventRequest;
import com.yifanfwu.locationevents.Models.EventResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestService {

    @GET("events")
    Call<ArrayList<EventResponse>> getEvents();

    @POST("events")
    Call<EventResponse> createEvent(@Body EventRequest eventRequest);

}
