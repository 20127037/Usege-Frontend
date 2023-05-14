package com.group_1.usege.album.services;

import androidx.annotation.Nullable;

import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.model.UserAlbum;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.model.UserFileInAlbum;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlbumService {

    @PUT("{id}/{name}")
    Single<Response<UserAlbum>> updateAlbum(@Path("id") String id, @Path("name") String name, @Body UserAlbum newAlbum);
    @POST("{id}/{name}")
    Single<Response<UserAlbum>> createAlbum(@Path("id") String id, @Path("name") String name, @Body UserAlbum userAlbum);
    @DELETE("{id}/{name}")
    Single<Response<UserAlbum>> deleteAlbum(@Path("id") String id, @Path("name") String name);
    @POST("{id}/{name}/images")
    Single<Response<List<UserFileInAlbum>>> addImagesToAlbum(@Path("id") String id, @Path("name") String name, @Query("file-names") String[] fileNames);
    @DELETE("{id}/{name}/images")
    Single<Response<List<UserFileInAlbum>>> removeImagesFromAlbum(@Path("id") String id, @Path("name") String name, @Query("file-names") String[] fileNames);
    @PUT("{id}/{to}/images")
    Single<Response<List<UserFileInAlbum>>>moveImagesToAlbum(@Path("id") String id, @Path("to") String to, @Query(value = "from", encoded = true) String from, @Query("file-names") String[] fileNames);
    @GET("{id}")
    Single<MasterFileService.QueryResponse<UserFile>> getAlbums(@Path("id") String id,
                                                               @Query("favourite") Boolean favourite,
                                                               @Query("limit") int limit,
                                                               @Nullable @Query(value = "attributes", encoded = true) String[] attributes,
                                                               @Nullable @Query(value = "lastKey", encoded = true) Map<String, String> lastKey);

}
