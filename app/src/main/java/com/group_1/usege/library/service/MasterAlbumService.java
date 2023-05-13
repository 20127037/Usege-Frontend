package com.group_1.usege.library.service;

import androidx.annotation.Nullable;

import com.group_1.usege.model.UserAlbum;
import com.group_1.usege.model.UserFile;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MasterAlbumService {

    @GET("{id}")
    Single<QueryResponse<UserAlbum>> getAlbums(@Path("id") String id, @Query("limit") int limit);

    @GET("{id}/{name}/images")
    Single<QueryResponse2<UserFile>> getAlbumFiles(@Path("id") String id,
                                         @Path("name") String name, @Query("limit") int limit);

    @Builder
    @Data
    class LoadFleRequestDto {
        private int limit;
        private String[] attributes;
        Map<String, String> lastKey;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class QueryResponse<S> {
        Map<String, String> nextEvaluatedKey;
        Map<String, String> prevEvaluatedKey;
        List<S> response;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class QueryResponse2<S> {
        Map<String, String> nextEvaluatedKey;
        Map<String, String> prevEvaluatedKey;
        List<S> response;
        String albumName;
    }
}

