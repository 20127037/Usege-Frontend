package com.group_1.usege.library.viewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;

import com.group_1.usege.R;
import com.group_1.usege.library.activities.OnlineLibraryActivity;
import com.group_1.usege.library.model.PexelsPageResponse;
import com.group_1.usege.library.model.PexelsPageResponseMapper;
import com.group_1.usege.library.paging.ImagePagingSource;
import com.group_1.usege.library.service.PexelsServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PexelsLibraryImageViewModel extends ImageViewModel<Integer, PexelsPageResponse> {

    public static final int PAGE_SIZE = 20;
    public static final int MAX_CACHE_PAGE = 3;

    @Inject
    PexelsLibraryImageViewModel(PexelsServiceGenerator pexelsServiceGenerator) {
        init(
                PAGE_SIZE,
                MAX_CACHE_PAGE * PAGE_SIZE,
                new ImagePagingSource<Integer, PexelsPageResponse>(
                        page -> pexelsServiceGenerator
                                .getService(R.string.key_pexels_api)
                                .searchPage(page, PAGE_SIZE),
                        new PexelsPageResponseMapper()) {
                    @Override
                    public Integer getFirstKey() {
                        return 1;
                    }
                });
    }
}