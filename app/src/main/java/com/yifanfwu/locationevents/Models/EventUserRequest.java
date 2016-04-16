package com.yifanfwu.locationevents.Models;

public class EventUserRequest {

    protected String userId;

    protected double latitude;

    protected double longitude;

    public EventUserRequest(String userId) {
        this.userId = userId;
    }
}
