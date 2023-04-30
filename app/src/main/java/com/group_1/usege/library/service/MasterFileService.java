package com.group_1.usege.library.service;

import android.content.Context;
import android.content.res.Resources;

import com.group_1.usege.R;
import com.group_1.usege.library.model.UserFile;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MasterFileService {

    @GET("{id}")
    Single<Response<QueryResponse<UserFile>>> getFiles(@Path("id") String id,
                                                       @Query("limit") int limit,
                                                       @Query("favourite") Boolean favourite,
                                                       @Query("deleted") Boolean deleted,
                                                       @Body LoadFleRequestDto requestDto);

    @GET("{id}/check")
    Single<Response<UserFile>> checkFile(@Path("id") String id,
                                         @Query("fileName") String fileName,
                                         @Query("uri") String uri);
    @Builder
    @Data
    class LoadFleRequestDto {
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
}

