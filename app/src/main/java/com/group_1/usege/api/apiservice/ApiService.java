package com.group_1.usege.api.apiservice;


import com.group_1.usege.api.googlemaps.GeocodeResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @GET("maps/api/geocode/json")
    Call<GeocodeResponse> getLocation(@Query("latlng") String latlng,
                                      @Query("key") String apiKey);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(@Part("userId")RequestBody userId,
                                  @Part MultipartBody.Part image);
}
