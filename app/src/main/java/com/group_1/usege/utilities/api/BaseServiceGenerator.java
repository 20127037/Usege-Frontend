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
    private S wrappedService;
    protected abstract String getBaseUrl();
    protected abstract Class<S> getServiceClass();

    private static final Retrofit.Builder builder
            = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create());
    private static final HttpLoggingInterceptor logging
            = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
    private static final OkHttpClient.Builder tokenHttpClient = new OkHttpClient.Builder();

    private void setRetrofitBuilderInfo(OkHttpClient httpClient)
    {
        builder.baseUrl(getBaseUrl())
                .client(httpClient);
    }
    private S createService()
    {
        setRetrofitBuilderInfo(httpClient);
        return builder.build().create(getServiceClass());
    }
    private S createService(final String token)
    {
        tokenHttpClient.interceptors().clear();
        tokenHttpClient.addInterceptor(logging);
        tokenHttpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder builder1 = original.newBuilder()
                    .header("Authorization", token);
            Request request = builder1.build();
            return chain.proceed(request);
        });
        setRetrofitBuilderInfo(tokenHttpClient.build());
        return builder.build().create(getServiceClass());
    }
    public S getService() {
        if (wrappedService == null)
            wrappedService = createService();
        return wrappedService;
    }
    public S getService(final String token)
    {
        if (wrappedService == null)
            wrappedService = createService(token);
        return wrappedService;
    }
}
