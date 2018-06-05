package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class Route {

    @SerializedName("overview_polyline")
    private Points polyline;

    @SerializedName("legs")
    private LegModel[] legs;

    public Points getPolyline() {
        return polyline;
    }

    public void setPolyline(Points polyline) {
        this.polyline = polyline;
    }

    public LegModel[] getLegs() {
        return legs;
    }

    public void setLegs(LegModel[] legs) {
        this.legs = legs;
    }
}
