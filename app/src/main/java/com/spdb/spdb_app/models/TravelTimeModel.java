package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class TravelTimeModel {
    @SerializedName("destination_addresses")
    private String[] destinationAddresses;
    @SerializedName("origin_addresses")
    private String[] originAddresses;
    @SerializedName("rows")
    private RowsModel[] elements;


    public String[] getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(String[] destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public String[] getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(String[] originAddresses) {
        this.originAddresses = originAddresses;
    }

    public RowsModel[] getElements() {
        return elements;
    }

    public void setElements(RowsModel[] elements) {
        this.elements = elements;
    }
}
