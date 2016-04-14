package com.yifanfwu.locationevents.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {


    @SerializedName("_id")
    @Expose
    public String Id;
    @SerializedName("timeMillis")
    @Expose
    public Integer timeMillis;
    @SerializedName("__v")
    @Expose
    public Integer V;
    @SerializedName("eventLocation")
    @Expose
    public EventLocation eventLocation;
    @SerializedName("users")
    @Expose
    public List<User> users = new ArrayList<User>();

}