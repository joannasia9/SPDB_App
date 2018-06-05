package com.spdb.spdb_app.models;

import com.google.gson.annotations.SerializedName;

public class LegModel {
    @SerializedName("steps")
    private StepModel[] steps;

    public StepModel[] getSteps() {
        return steps;
    }

    public void setSteps(StepModel[] steps) {
        this.steps = steps;
    }
}
