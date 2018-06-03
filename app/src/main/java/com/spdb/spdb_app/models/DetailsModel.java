package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class DetailsModel {
    @SerializedName("formatted_address")
    private String address;
    @SerializedName("formatted_phone_number")
    private String phoneNumber;
    @SerializedName("geometry")
    private GeometryModel geometry;
    @SerializedName("name")
    private String placeName;
    @SerializedName("rating")
    private float rating;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public GeometryModel getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryModel geometry) {
        this.geometry = geometry;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
