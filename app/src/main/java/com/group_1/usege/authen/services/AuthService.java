package com.group_1.usege.authen.services;

import com.group_1.usege.account.dto.CreateAccountRequestDto;
import com.group_1.usege.authen.model.CacheToken;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AuthService {
    @POST(".")
    Single<Response<CacheToken>> login(@Body CreateAccountRequestDto request);
    @PUT(".")
    Single<Response<CacheToken>> refresh(@Query("refresh_token") String refreshToken);
}
