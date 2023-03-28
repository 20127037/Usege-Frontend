package com.group_1.usege.api.googlemaps;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallApi {
    String address = "";

    public CallApi() {
        // Do nothing
    }

    public String callApiGetAddress(String location) {
        String latlng = location;
        String apiKey = "AIzaSyB4sqKufBLlfC9vIat2DKDIYNJsAYB5zRc";
        // Khởi tạo retrofit
        ApiService apiService = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        Call<GeocodeResponse> call = apiService.getLocation(latlng, apiKey);

        call.enqueue(new Callback<GeocodeResponse>() {
            @Override
            public void onResponse(Call<GeocodeResponse> call, Response<GeocodeResponse> response) {
                if (response.isSuccessful()) {
                    GeocodeResponse geocodeResponse = response.body();
                    if (response.isSuccessful() && geocodeResponse != null) {
                        // Lấy tên thành phố
                        for (Result result : geocodeResponse.getResults()) {
                            for (AddressComponent component : result.getAddressComponents()) {
                                if (component.getTypes().contains("administrative_area_level_1")) {
                                    address = component.getLongName();
                                }
                            }
                        }
                    }
                } else {
                    Log.e("GeocodeApi", "Request failed");
                }
            }

            @Override
            public void onFailure(Call<GeocodeResponse> call, Throwable t) {
                Log.e("GeocodeApi", "Request failed: " + t.getMessage());
            }
        });
        //Log.e("Location", "Address " + address);
        return address;
    }
}
