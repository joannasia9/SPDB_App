package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class ViewPortModel {
    @SerializedName("northeast")
    private LocationModel northEastViewport;
    @SerializedName("southwest")
    private LocationModel southWestViewport;

    public LocationModel getNorthEastViewport() {
        return northEastViewport;
    }

    public void setNorthEastViewport(LocationModel northEastViewport) {
        this.northEastViewport = northEastViewport;
    }

    public LocationModel getSouthWestViewport() {
        return southWestViewport;
    }

    public void setSouthWestViewport(LocationModel southWestViewport) {
        this.southWestViewport = southWestViewport;
    }
}
