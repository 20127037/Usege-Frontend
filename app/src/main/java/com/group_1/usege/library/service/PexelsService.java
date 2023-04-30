package com.group_1.usege.library.service;

import com.group_1.usege.library.model.PexelsPageResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PexelsService {
    @GET("curated")
    Single<PexelsPageResponse> searchPage(@Query("page") Integer page, @Query("per_page") Integer perPage);
    @GET("search")
    Single<PexelsPageResponse> searchPage(@Query("query") String query, @Query("page") Integer page, @Query("per_page") Integer perPage);
}
