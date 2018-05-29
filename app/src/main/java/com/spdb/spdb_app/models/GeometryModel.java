package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class GeometryModel {
    @SerializedName("location")
    private LocationModel locationModel;

    @SerializedName("viewport")
    private ViewPortModel viewPortModel;

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public ViewPortModel getViewPortModel() {
        return viewPortModel;
    }

    public void setViewPortModel(ViewPortModel viewPortModel) {
        this.viewPortModel = viewPortModel;
    }
}
