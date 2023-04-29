package com.group_1.usege.library.service;

import com.group_1.usege.library.model.PexelsPageResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PexelsService {
    @GET("curated")
    Single<PexelsPageResponse> getPage(@Query("page") int page, @Query("per_page") int perPage);
}
