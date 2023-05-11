package com.group_1.usege.utilities.api;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import com.group_1.usege.R;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Inherit this class to make a new service generator (reused retrofit client)
 * @param <S> type of the service class
 */
public abstract class BaseServiceGenerator<S> {

    private S wrappedService;
    private String currentToken;
    protected abstract Class<S> getServiceClass();
    protected final String baseUrl;
    private final Resources resources;
    public BaseServiceGenerator(Resources resources, @StringRes int versionRes, @StringRes int serviceNameRes)
    {
        this(resources, R.string.uri_base_server, versionRes, serviceNameRes);
    }
    public BaseServiceGenerator(Resources resources, @StringRes int baseUrlResId)
    {
        this.resources = resources;
        this.baseUrl = resources.getString(baseUrlResId);
    }

    public BaseServiceGenerator(Resources resources, @StringRes int domainRes, @StringRes int versionRes, @StringRes int serviceNameRes)
    {
        this.resources = resources;
        baseUrl = String.format("%s/%s/%s/", resources.getString(domainRes), resources.getString(versionRes), resources.getString(serviceNameRes));
    }

    public BaseServiceGenerator(Resources resources, @StringRes int versionRes, @StringRes int serviceNameRes, String baseUrl)
    {
        this.baseUrl = String.format("%s/%s/%s/", baseUrl, resources.getString(versionRes), resources.getString(serviceNameRes));
    }

    private static final Retrofit.Builder builder
            = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()));
    private static final HttpLoggingInterceptor logging
            = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
    private static final OkHttpClient.Builder tokenHttpClient = new OkHttpClient.Builder();

    private void setRetrofitBuilderInfo(OkHttpClient httpClient)
    {
        builder.baseUrl(baseUrl)
                .client(httpClient);
    }
    private S createService()
    {
        setRetrofitBuilderInfo(httpClient);
        return builder.build().create(getServiceClass());
    }

    private static synchronized OkHttpClient addTokenHeader(final String token)
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
        return tokenHttpClient.build();
    }
    private S createService(final String token)
    {
        currentToken = token;
        setRetrofitBuilderInfo(addTokenHeader(token));
        return builder.build().create(getServiceClass());
    }
    public synchronized S getService() {
        if (wrappedService == null)
            wrappedService = createService();
        return wrappedService;
    }
    public synchronized S getService(final String token)
    {
        if (wrappedService == null || !currentToken.equals(token))
            wrappedService = createService(token);
        //A new token is used -> build a new service
        return wrappedService;
    }
    public synchronized S getService(@StringRes final int tokenResId)
    {
        String token = resources.getString(tokenResId);
        return getService(token);
    }
}
