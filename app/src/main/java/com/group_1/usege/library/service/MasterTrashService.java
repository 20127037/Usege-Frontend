package com.group_1.usege.library.service;

import androidx.annotation.Nullable;

import com.group_1.usege.model.UserFile;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MasterTrashService {
    @GET("{id}")
    Single<QueryResponse<UserFile>> getTrashFiles(@Path("id") String id,
                                                  @Query("limit") int limit,
                                                  @Nullable @Query(value = "lastKey", encoded = true) Map<String, String> lastKey);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class QueryResponse<S> {
        Map<String, String> nextEvaluatedKey;
        Map<String, String> prevEvaluatedKey;
        List<S> response;
    }
}
