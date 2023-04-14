package com.group_1.usege.api.googlemaps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodeResponse {
    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }
}
