package com.group_1.usege.authentication.services;

import com.group_1.usege.authentication.model.CreateAccountRequestDto;
import com.group_1.usege.authentication.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {
    @POST
    Call<UserInfo> getUser(@Body CreateAccountRequestDto accountRequestDto);
}
