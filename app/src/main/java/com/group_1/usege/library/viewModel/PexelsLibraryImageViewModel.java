package com.group_1.usege.library.viewModel;

import com.group_1.usege.library.model.PexelsPageResponse;
import com.group_1.usege.library.model.PexelsPageResponseMapper;
import com.group_1.usege.library.paging.ImagePagingSource;
import com.group_1.usege.library.paging.PagingProvider;


public class PexelsLibraryImageViewModel extends ImageViewModel<Integer, PexelsPageResponse> {

    public static final int PAGE_SIZE = 20;
    public static final int MAX_CACHE_PAGE = 3;


    public void setCurrentProvider(PagingProvider<Integer, PexelsPageResponse> currentProvider) {
        this.currentProvider = currentProvider;
    }
    private PagingProvider<Integer, PexelsPageResponse> currentProvider;

    public void init()
    {
        super.init(
                PAGE_SIZE,
                MAX_CACHE_PAGE * PAGE_SIZE,
                () -> new ImagePagingSource<Integer, PexelsPageResponse>(
                        p -> currentProvider.paging(p),
                        new PexelsPageResponseMapper()) {
                    @Override
                    public Integer getFirstKey() {
                        return 1;
                    }
                });
    }
}