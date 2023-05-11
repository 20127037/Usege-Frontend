package com.group_1.usege.api.apiservice;

import androidx.annotation.Nullable;

import com.group_1.usege.dto.LoadFileRequestDto;
import com.group_1.usege.model.UserFile;

import java.util.List;
import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MasterFileService {
    @GET("{id}")
    Call<QueryResponse<UserFile>> getFiles(@Path("id") String id,
                                                     @Query("favourite") Boolean favourite,
                                                     @Query("limit") int limit,
                                                     @Nullable @Query(value = "attributes", encoded = true) String[] attributes,
                                                     @Nullable @Query(value = "lastKey", encoded = true) Map<String, String> lastKey);

    @GET("{id}/check")
    Call<Response<UserFile>> checkFile(@Path("id") String id,
                                         @Query("fileName") String fileName,
                                         @Query("uri") String uri);
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
}
