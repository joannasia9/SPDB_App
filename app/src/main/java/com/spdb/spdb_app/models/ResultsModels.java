package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class ResultsModels {
    @SerializedName("results")
    private PlaceModel[] placeModels;

    public PlaceModel[] getPlaceModels() {
        return placeModels;
    }

    public void setPlaceModels(PlaceModel[] placeModels) {
        this.placeModels = placeModels;
    }
}
