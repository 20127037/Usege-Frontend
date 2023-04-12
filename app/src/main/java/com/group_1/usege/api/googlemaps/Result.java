package com.group_1.usege.api.googlemaps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("address_components")
    private List<AddressComponent> addressComponents;

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }
}
