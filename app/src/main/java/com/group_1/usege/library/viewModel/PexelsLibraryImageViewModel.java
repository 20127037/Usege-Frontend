package com.group_1.usege.library.viewModel;

import com.group_1.usege.R;
import com.group_1.usege.library.activities.OnlineLibraryActivity;
import com.group_1.usege.library.model.PexelsPageResponseMapper;
import com.group_1.usege.library.paging.ImagePagingSource;
import com.group_1.usege.library.service.PexelsServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PexelsLibraryImageViewModel extends ImageViewModel {

    @Inject
    PexelsLibraryImageViewModel(PexelsServiceGenerator pexelsServiceGenerator) {
        int itemPerPageCount = OnlineLibraryActivity.SPAN_COUNT * OnlineLibraryActivity.ROW_COUNT;
        init(
                itemPerPageCount,
                itemPerPageCount * 3,
                new ImagePagingSource<>(
                        page -> pexelsServiceGenerator
                                .getService(R.string.key_pexels_api)
                                .getPage(page, itemPerPageCount),
                        new PexelsPageResponseMapper()));
    }
}
