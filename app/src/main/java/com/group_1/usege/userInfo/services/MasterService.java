package com.group_1.usege.userInfo.services;

import com.group_1.usege.userInfo.model.UserInfo;
import com.group_1.usege.userInfo.model.UserPlan;
import com.group_1.usege.userInfo.model.UserStatistic;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MasterService {
    @GET("master/user/{id}")
    Single<Response<UserInfo>> getUserInfo(@Path("id") String id);
    @GET("master/user/statistic/{id}")
    Single<Response<UserStatistic>> getUserStatistic(@Path("id") String id);
    @GET("master/user/plan/{id}")
    Single<Response<UserPlan>> getUserPlan(@Path("id") String id);
}
