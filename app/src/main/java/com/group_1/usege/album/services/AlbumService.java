package com.group_1.usege.album.services;

import com.group_1.usege.account.dto.ConfirmForgetPasswordDto;
import com.group_1.usege.account.dto.CreateAccountRequestDto;
import com.group_1.usege.userInfo.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlbumService {

//    @POST(".")
//    Single<Response<UserInfo>> create(@Body CreateAccountRequestDto accountRequestDto);
//    @PUT("{id}")
//    Single<Response<Void>> confirm(@Path("id") String id, @Query("code") String code);



    @POST("/{id}/{name}")
    Single<Response<Void>> createAlbum(@Path("id") String id, @Path("name") String name);
    @DELETE("/{id}/{name}")
    Single<Response<Void>> deleteAlbum(@Path("id") String id, @Path("name") String name);
    @POST("/{id}/{name}/images")
    Single<Response<Void>> addImagesToAlbum(@Path("id") String id, @Path("name") String name, @Query("file-names") List<String> fileNames);
    @DELETE("/{id}/{name}/images")
    Single<Response<Void>> removeImagesFromAlbum(@Path("id") String id, @Path("name") String name, @Query("file-names") List<String> fileNames);
    @PUT("/{id}/{to}/images")
    Single<Response<Void>> moveImagesToAlbum(@Path("id") String id, @Path("to") String to, @Path("from") String from, @Query("file-names") List<String> fileNames);

//    @POST("forget/{id}")
//    Single<Response<Void>> forgetPassword(@Path("id") String id);
//    @PUT("forget/{id}")
//    Single<Response<Void>> confirmForgetPassword(@Path("id") String id, @Body ConfirmForgetPasswordDto code);
}
