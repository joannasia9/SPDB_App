package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class RowsModel {
    @SerializedName("elements")
    private ElementModel[] elements;

    public ElementModel[] getElements() {
        return elements;
    }

    public void setElements(ElementModel[] elements) {
        this.elements = elements;
    }
}
