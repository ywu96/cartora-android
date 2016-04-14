package com.yifanfwu.locationevents.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventLocation {

    @SerializedName("latitude")
    @Expose
    public Integer latitude;
    @SerializedName("longitude")
    @Expose
    public Integer longitude;

}