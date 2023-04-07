package com.group_1.usege.api.apiservice;

import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;
import com.group_1.usege.api.googlemaps.AddressComponent;
import com.group_1.usege.api.googlemaps.GeocodeResponse;
import com.group_1.usege.api.googlemaps.Result;
import com.group_1.usege.modle.Image;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGoogleMap {

    String address = "";
    private String location;
    private Image image;
    private Context context;

    public ApiGoogleMap(String location, Image image, Context context) {
        this.location = location;
        this.image = image;
        this.context = context;
    }

    public void callApiGetAddress() {

        String latlng = location;
        String apiKey = "AIzaSyB4sqKufBLlfC9vIat2DKDIYNJsAYB5zRc";
        //String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latlng + "&key=" + apiKey;
        //Log.e("Location", "Address2 " + address);

         //Khởi tạo retrofit
        ApiService apiService = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        Call<GeocodeResponse> call = apiService.getLocation(latlng, apiKey);

        call.enqueue(new Callback<GeocodeResponse>() {
            @Override
            public void onResponse(Call<GeocodeResponse> call, retrofit2.Response<GeocodeResponse> response) {
                if (response.isSuccessful()) {
                    GeocodeResponse geocodeResponse = response.body();
                    if (geocodeResponse != null) {
                        // Lấy tên thành phố
                        for (Result result : geocodeResponse.getResults()) {
                            for (AddressComponent component : result.getAddressComponents()) {
                                if (component.getTypes().contains("administrative_area_level_1")) {
                                    address = component.getLongName();
                                    image.setLocation(address);
                                    return;
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

//        GetAddress getAddress = new GetAddress(latlng, apiKey, address, context);
//        getAddress.start();

        //Log.e("Location", "Address1 " + address);
        //return address;
    }

//    public class GetAddress extends Thread {
//        private String latlng;
//        private String apiKey;
//        private Context context;
//
//        private String address = "";
//
//        private GetAddress(String latlng, String apiKey, String address, Context context) {
//            this.latlng = latlng;
//            this.apiKey = apiKey;
//            this.address = address;
//            this.context = context;
//        }
//        @Override
//        public void run() {
//            // Khởi tạo retrofit
//            ApiService apiService = new Retrofit.Builder()
//                    .baseUrl("https://maps.googleapis.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                    .create(ApiService.class);
//
//            Call<GeocodeResponse> call = apiService.getLocation(latlng, apiKey);
//
//            call.enqueue(new Callback<GeocodeResponse>() {
//                @Override
//                public void onResponse(Call<GeocodeResponse> call, retrofit2.Response<GeocodeResponse> response) {
//                    if (response.isSuccessful()) {
//                        GeocodeResponse geocodeResponse = response.body();
//                        if (geocodeResponse != null) {
//                            // Lấy tên thành phố
//                            for (Result result : geocodeResponse.getResults()) {
//                                for (AddressComponent component : result.getAddressComponents()) {
//                                    if (component.getTypes().contains("administrative_area_level_1")) {
//                                        address = component.getLongName();
//                                        return;
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        Log.e("GeocodeApi", "Request failed");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GeocodeResponse> call, Throwable t) {
//                    Log.e("GeocodeApi", "Request failed: " + t.getMessage());
//                }
//            });
//
//            while (address.length() == 0) {
//                // Do nothing
//            }
//            image.setLocation(address);
//        }
//    }
}
