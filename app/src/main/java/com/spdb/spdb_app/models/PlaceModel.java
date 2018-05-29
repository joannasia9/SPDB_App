package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class PlaceModel {
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("geometry")
    private GeometryModel geometryModel;
    @SerializedName("name")
    private String placeName;
    @SerializedName("types")
    private String[] placeTypes;
    @SerializedName("rating")
    private float rating;
    @SerializedName("formatted_address")
    private String formattedAddress;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public GeometryModel getGeometryModel() {
        return geometryModel;
    }

    public void setGeometryModel(GeometryModel geometryModel) {
        this.geometryModel = geometryModel;
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
