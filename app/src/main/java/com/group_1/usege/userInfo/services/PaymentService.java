package com.group_1.usege.userInfo.services;

import com.group_1.usege.userInfo.dtos.PaymentRequestDto;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentService {
    @POST("{id}")
    Single<Response<Void>> payment(@Path("id") String id, @Body PaymentRequestDto requestDto);
}
