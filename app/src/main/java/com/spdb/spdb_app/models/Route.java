package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class Route {

    @SerializedName("overview_polyline")
    private Points polyline;

    public Points getPolyline() {
        return polyline;
    }

    public void setPolyline(Points polyline) {
        this.polyline = polyline;
    }
}
