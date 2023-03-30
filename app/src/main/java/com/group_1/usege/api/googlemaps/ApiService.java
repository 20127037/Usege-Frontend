package com.group_1.usege.api.googlemaps;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("maps/api/geocode/json")
    Call<GeocodeResponse> getLocation(@Query("latlng") String latlng,
                                      @Query("key") String apiKey);

}
