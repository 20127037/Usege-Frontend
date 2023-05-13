package com.group_1.usege.album.services;

import androidx.annotation.Nullable;

import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.model.UserAlbum;
import com.group_1.usege.model.UserFile;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlbumService {

//    @POST(".")
//    Single<Response<UserInfo>> create(@Body CreateAccountRequestDto accountRequestDto);
//    @PUT("{id}")
//    Single<Response<Void>> confirm(@Path("id") String id, @Query("code") String code);



    @POST("{id}/{name}")
    Single<Response<UserAlbum>> createAlbum(@Path("id") String id, @Path("name") String name);
    @DELETE("{id}/{name}")
    Single<Response<Void>> deleteAlbum(@Path("id") String id, @Path("name") String name);
    @POST("{id}/{name}/images")
    Single<Response<Void>> addImagesToAlbum(@Path("id") String id, @Path("name") String name, @Query("file-names") List<String> fileNames);
    @DELETE("{id}/{name}/images")
    Single<Response<Void>> removeImagesFromAlbum(@Path("id") String id, @Path("name") String name, @Query("file-names") List<String> fileNames);
    @PUT("{id}/{to}/images")
    Single<Response<Void>> moveImagesToAlbum(@Path("id") String id, @Path("to") String to, @Path("from") String from, @Query("file-names") List<String> fileNames);
    @GET("{id}")
    Single<MasterFileService.QueryResponse<UserFile>> getAlbums(@Path("id") String id,
                                                               @Query("favourite") Boolean favourite,
                                                               @Query("limit") int limit,
                                                               @Nullable @Query(value = "attributes", encoded = true) String[] attributes,
                                                               @Nullable @Query(value = "lastKey", encoded = true) Map<String, String> lastKey);

}
