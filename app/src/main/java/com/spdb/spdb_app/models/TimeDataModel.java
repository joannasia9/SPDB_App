package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class TimeDataModel {
    @SerializedName("text")
    private String textValue;

    @SerializedName("value")
    private long valueValue;

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public long getValueValue() {
        return valueValue;
    }

    public void setValueValue(long valueValue) {
        this.valueValue = valueValue;
    }
}

