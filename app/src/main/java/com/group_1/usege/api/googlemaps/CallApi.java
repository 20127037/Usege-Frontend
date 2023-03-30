package com.group_1.usege.api.googlemaps;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.group_1.usege.modle.Image;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallApi {

    private String location;
    private Image image;
    private Context context;

    public CallApi(String location, Image image, Context context) {
        this.location = location;
        this.image = image;
        this.context = context;
    }

    public CallApi() {
        // Do nothing
    }

    public String callApiGetAddress() {
        String address = "";
        String latlng = location;
        String apiKey = "AIzaSyB4sqKufBLlfC9vIat2DKDIYNJsAYB5zRc";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latlng + "&key=" + apiKey;
        //Log.e("Location", "Address2 " + address);

        GetAddress getAddress = new GetAddress(url, address, context);
        getAddress.start();

        // Khởi tạo retrofit
//        ApiService apiService = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(ApiService.class);
//
//
//
//        Call<GeocodeResponse> call = apiService.getLocation(latlng, apiKey);
//
//        call.enqueue(new Callback<GeocodeResponse>() {
//            @Override
//            public void onResponse(Call<GeocodeResponse> call, Response<GeocodeResponse> response) {
//                if (response.isSuccessful()) {
//                    GeocodeResponse geocodeResponse = response.body();
//                    if (geocodeResponse != null) {
//                        // Lấy tên thành phố
//                        for (Result result : geocodeResponse.getResults()) {
//                            for (AddressComponent component : result.getAddressComponents()) {
//                                if (component.getTypes().contains("administrative_area_level_1")) {
//                                    address = component.getLongName();
//                                    Log.e("Location", "Address " + address);
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    Log.e("GeocodeApi", "Request failed");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GeocodeResponse> call, Throwable t) {
//                Log.e("GeocodeApi", "Request failed: " + t.getMessage());
//            }
//        });

//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                GeocodeResponse geocodeResponse = new Gson().fromJson(response,  GeocodeResponse.class);
//                if (geocodeResponse != null) {
//                    // Lấy tên thành phố
//                    for (Result result : geocodeResponse.getResults()) {
//                        for (AddressComponent component : result.getAddressComponents()) {
//                            if (component.getTypes().contains("administrative_area_level_1")) {
//                                address = component.getLongName();
//                                Log.e("Location", "Address " + address);
//                                return;
//                            }
//                        }
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("ERROR", "Error getting address: " + error.getMessage());
//            }
//        });
//
//        requestQueue.add(stringRequest);

        //Log.e("Location", "Address1 " + address);
        return address;
    }

    public class GetAddress extends Thread {
        private String url;
        private Context context;

        private String address = "";

        private GetAddress(String url, String address, Context context) {
            this.url = url;
            this.address = address;
            this.context = context;
        }
        @Override
        public void run() {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    GeocodeResponse geocodeResponse = new Gson().fromJson(response,  GeocodeResponse.class);
                    if (geocodeResponse != null) {
                        // Lấy tên thành phố
                        for (Result result : geocodeResponse.getResults()) {
                            for (AddressComponent component : result.getAddressComponents()) {
                                if (component.getTypes().contains("administrative_area_level_1")) {
                                    address = component.getLongName();

                                    return;
                                }
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", "Error getting address: " + error.getMessage());
                }
            });

            requestQueue.add(stringRequest);

            while (address.length() == 0) {
                //
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            image.setLocation(address);
        }
    }
}
