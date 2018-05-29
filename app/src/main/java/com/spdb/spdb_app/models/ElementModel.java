package com.spdb.spdb_app.models;


import com.google.gson.annotations.SerializedName;

public class ElementModel {
    @SerializedName("distance")
    private TimeDataModel distance;

    @SerializedName("duration")
    private TimeDataModel duration;

    public TimeDataModel getDistance() {
        return distance;
    }

    public void setDistance(TimeDataModel distance) {
        this.distance = distance;
    }

    public TimeDataModel getDuration() {
        return duration;
    }

    public void setDuration(TimeDataModel duration) {
        this.duration = duration;
    }
}
