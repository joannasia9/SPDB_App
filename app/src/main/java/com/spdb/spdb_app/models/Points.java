package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

class Points {
    @SerializedName("points")
    String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
