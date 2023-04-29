package com.group_1.usege.library.paging;

import io.reactivex.rxjava3.core.Single;

public interface PagingProvider<TPageKey, TResponse> {
    Single<TResponse> paging(TPageKey key);
}
