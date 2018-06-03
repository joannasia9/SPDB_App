package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class RouteModel {
    @SerializedName("routes")
    private Route[] routes;

    public Route[] getRoutes() {
        return routes;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }
}
