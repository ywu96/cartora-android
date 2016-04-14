package com.yifanfwu.locationevents.Rest;

import com.yifanfwu.locationevents.Models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestService {

    @GET("/events")
    Call<List<Event>> getEvents();

}
