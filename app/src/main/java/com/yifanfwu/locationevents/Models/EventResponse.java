package com.yifanfwu.locationevents.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EventResponse {

    @SerializedName("_id")
    protected String Id;

    @SerializedName("eventName")
    protected String eventName;

    @SerializedName("timeMillis")
    protected Integer timeMillis;

    @SerializedName("__v")
    protected Integer V;

    @SerializedName("eventLocation")
    protected EventLocation eventLocation;

    @SerializedName("users")
    protected List<EventUserResponse> eventUserResponses = new ArrayList<>();

    public String getId() {
        return this.Id;
    }

    public String getEventName() {
        return this.eventName;
    }

    public Integer getTimeMillis() {
        return this.timeMillis;
    }

    public Integer getV() {
        return this.V;
    }

    public EventLocation getEventLocation() {
        return this.eventLocation;
    }

    public List<EventUserResponse> getEventUserResponses() {
        return this.eventUserResponses;
    }
}