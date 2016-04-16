package com.yifanfwu.locationevents.Models;


import java.util.ArrayList;

public class EventRequest {

    protected String eventName;

    protected EventLocation eventLocation;

    protected ArrayList<EventUserRequest> users;

    protected Integer timeMillis;

    public EventRequest(String eventName, ArrayList<EventUserRequest> users, double lat, double lng, Integer time) {
        this.eventName = eventName;
        this.users = users;
        this.eventLocation = new EventLocation(lat, lng);
        this.timeMillis = time;
    }
}
