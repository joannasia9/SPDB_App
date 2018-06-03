package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class PlaceDetailsModel {
    @SerializedName("result")
    private DetailsModel result;

    public DetailsModel getResult() {
        return result;
    }

    public void setResult(DetailsModel result) {
        this.result = result;
    }
}
