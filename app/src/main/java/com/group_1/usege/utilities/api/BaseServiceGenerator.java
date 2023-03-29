package com.group_1.usege.utilities.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Inherit this class to make a new service generator (reused retrofit client)
 * @param <S> type of the service class
 */
public abstract class BaseServiceGenerator<S> {

    //protected static final String BASE_URL = "https://api.github.com/";
    protected abstract String getBaseUrl();
    private final Retrofit.Builder builder
            = new Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create());
    private Retrofit retrofit = builder.build();
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final HttpLoggingInterceptor logging
            = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    public S createService(Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
    public S createService(Class<S> serviceClass, final String token) {
        if (token != null) {
            httpClient.interceptors().clear();
            httpClient.addInterceptor( chain -> {
                Request original = chain.request();
                Request.Builder builder1 = original.newBuilder()
                        .header("Authorization", token);
                Request request = builder1.build();
                return chain.proceed(request);
            });
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
