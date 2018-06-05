package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class StepModel {
    @SerializedName("start_location")
    private LocationModel startLocation;

    @SerializedName("end_location")
    private LocationModel endLocation;

    @SerializedName("polyline")
    private Points polyline;

    public LocationModel getStartLocation() {
        return startLocation;
    }

    public LocationModel getEndLocation() {
        return endLocation;
    }

    public Points getPolyline() {
        return polyline;
    }
}
