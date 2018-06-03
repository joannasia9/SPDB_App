package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class ResultsModels {
    @SerializedName("results")
    private PlaceModel[] placeModels;

    @SerializedName("next_page_token")
    private String nextPageToken;

    public PlaceModel[] getPlaceModels() {
        return placeModels;
    }

    public void setPlaceModels(PlaceModel[] placeModels) {
        this.placeModels = placeModels;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }
}
