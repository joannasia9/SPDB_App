package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class Points {
    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
