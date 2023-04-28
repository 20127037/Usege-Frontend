package com.group_1.usege.library.viewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.group_1.usege.library.paging.ImagePagingSource;
import com.group_1.usege.model.Image;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class ImageViewModel extends ViewModel {
    public Flowable<PagingData<Image>> imagePagingDataFlowable;
    public ImageViewModel(int itemPerPageCount, int maxItemCount, ImagePagingSource imagePagingSource) {
        Pager<String, Image> pager = new Pager<>(
                // Create new paging config
                new PagingConfig(itemPerPageCount, //  Count of items in one page
                        itemPerPageCount, //  Number of items to prefetch
                        false, // Enable placeholders for data which is not yet loaded
                        itemPerPageCount, // initialLoadSize - Count of items to be loaded initially
                        maxItemCount),
                () -> imagePagingSource); // set paging source
        // inti Flowable
        imagePagingDataFlowable = PagingRx.getFlowable(pager);
        CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(imagePagingDataFlowable, coroutineScope);
    }
}
