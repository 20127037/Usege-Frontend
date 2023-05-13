package com.group_1.usege.library.service;

import com.group_1.usege.model.UserFile;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrashService {

    @POST("{id}")
    Single<Response<List<UserFile>>> deleteFiles(@Path("id") String id, @Query("file-names") String[] fileNames);
    @POST("{id}/all")
    Single<Response<List<UserFile>>> deleteAllFiles(@Path("id") String id);
    @DELETE("{id}")
    Single<Response<List<UserFile>>> clearFiles(@Path("id") String id, @Query("file-names") String[] fileNames);
    @DELETE("{id}/all")
    Single<Response<List<UserFile>>> clearAllFiles(@Path("id") String id);
    @PUT("{id}")
    Single<Response<List<UserFile>>> restoreFiles(@Path("id") String id, @Query("file-names") String[] fileNames);
    @PUT("{id}/all")
    Single<Response<List<UserFile>>> restoreAllFiles(@Path("id") String id);
}
