package com.group_1.usege.library.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.group_1.usege.library.model.ImagePaging;
import com.group_1.usege.model.Image;
import com.group_1.usege.utilities.mappers.Mapper;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * Manager image sourcing from an external storage
 * @param <TPageKey> type of key for pagination
 * @param <TResponse> type of paging response Dto
 */
public class ImagePagingSource<TPageKey, TResponse> extends RxPagingSource<TPageKey, Image> {
    @NonNull
    private final PagingProvider<TPageKey, TResponse> provider;
    private final Mapper<TResponse, ImagePaging<TPageKey>> dtoMapper;

    ImagePagingSource(@NonNull PagingProvider<TPageKey, TResponse> provider, Mapper<TResponse, ImagePaging<TPageKey>> mapper) {
        this.provider = provider;
        this.dtoMapper = mapper;
    }

    @NotNull
    @Override
    public Single<LoadResult<TPageKey, Image>> loadSingle(
            @NotNull LoadParams<TPageKey> params) {
        // Start refresh at page 1 if undefined.
        TPageKey nextPageNumber = params.getKey();
        return provider.paging(nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(dtoMapper::map)
                .map(this::toLoadResult)
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<TPageKey, Image> toLoadResult(@NonNull ImagePaging<TPageKey> response) {
        return new LoadResult.Page<>(
                response.getImages(),
                null, // Only paging forward.
                response.getNextPage());
    }

    @Nullable
    @Override
    public TPageKey getRefreshKey(@NonNull PagingState<TPageKey, Image> pagingState) {
        return null;
    }
}


