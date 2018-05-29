package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class DistanceModel{
    @SerializedName("value")
    private long value;
    @SerializedName("text")
    private String text;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
