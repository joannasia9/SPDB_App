package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class PlaceModel {
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("name")
    private String placeName;
    @SerializedName("types")
    private String[] placeTypes;
    @SerializedName("rating")
    private float rating;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String[] getPlaceTypes() {
        return placeTypes;
    }

    public void setPlaceTypes(String[] placeTypes) {
        this.placeTypes = placeTypes;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
