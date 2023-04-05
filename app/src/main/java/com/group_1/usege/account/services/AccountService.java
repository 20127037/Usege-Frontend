package com.group_1.usege.account.services;

import com.group_1.usege.account.dto.CreateAccountRequestDto;
import com.group_1.usege.account.dto.ConfirmForgetPasswordDto;
import com.group_1.usege.userInfo.model.UserInfo;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountService {
    String USERNAME = "USERNAME";
    String USERNAME_EXISTS = "USERNAME_EXISTS";
    String PASSWORD_INVALID =  "PASSWORD_INVALID";
    String CODE_MISMATCH =  "CODE_MISMATCH";
    String CODE_EXPIRED = "CODE_EXPIRED";
    String USER_NOT_FOUND = "USER_NOT_FOUND";

    @POST(".")
    Single<Response<UserInfo>> create(@Body CreateAccountRequestDto accountRequestDto);
    @PUT("{id}")
    Single<Response<Void>> confirm(@Path("id") String id, @Query("code") String code);
    @POST("confirmCode/{id}")
    Single<Response<Void>> resendAccountConfirmCode(@Path("id") String id);
    @POST("forgetPassword/{id}")
    Single<Response<Void>> forgetPassword(@Path("id") String id);
    @POST("forgetPassword/confirmCode/{id}")
    Single<Response<Void>> resendForgetPasswordConfirmCode(@Path("id") String id);
    @PUT("forgetPassword/{id}")
    Single<Response<Void>> confirmForgetPassword(@Path("id") String id, @Body ConfirmForgetPasswordDto code);
}
