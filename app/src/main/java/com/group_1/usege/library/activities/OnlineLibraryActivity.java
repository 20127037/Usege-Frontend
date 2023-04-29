package com.group_1.usege.library.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.group_1.usege.R;
import com.group_1.usege.library.adapter.SimpleImagesAdapter;
import com.group_1.usege.library.utilities.ImageComparator;
import com.group_1.usege.library.viewModel.PexelsLibraryImageViewModel;
import com.group_1.usege.utilities.adapter.LoadStateAdapter;
import com.group_1.usege.utilities.view.GridSpaceDecorator;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnlineLibraryActivity extends AppCompatActivity {
    public static final int SPAN_COUNT = 3;
    public static final int ROW_COUNT = 4;
    @Inject
    public RequestManager requestManager;
    @Inject
    public ImageComparator comparator;
    @Inject
    public LoadStateAdapter loadStateAdapter;
    private SimpleImagesAdapter imageAdapter;

    public OnlineLibraryActivity()
    {
        super(R.layout.activity_online_library);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SearchView searchView = findViewById(R.id.view_search);
        RecyclerView recyclerView = findViewById(R.id.lst_img);

        // Create new MoviesAdapter object and provide
        imageAdapter = new SimpleImagesAdapter(comparator, requestManager, null);
        PexelsLibraryImageViewModel mainViewModel = new ViewModelProvider(this).get(PexelsLibraryImageViewModel.class);

        //set recyclerview and adapter
        initRecyclerviewAndAdapter(recyclerView);

        // Subscribe to to paging data
        mainViewModel.getImagePagingDataFlowable()
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(moviePagingData -> {
            // submit new data to recyclerview adapter
            imageAdapter.submitData(getLifecycle(), moviePagingData);
        });
    }

    private void initRecyclerviewAndAdapter(RecyclerView recyclerView) {
        // Create GridlayoutManger with span of count of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        // Finally set LayoutManger to recyclerview
        recyclerView.setLayoutManager(gridLayoutManager);

        // Add ItemDecoration to add space between recyclerview items
        recyclerView.addItemDecoration(new GridSpaceDecorator(SPAN_COUNT, 20, true));

        // set adapter
        recyclerView.setAdapter(
                // This will show end user a progress bar while pages are being requested from server
                imageAdapter.withLoadStateFooter(
                        // When we will scroll down and next page request will be sent
                        // while we get response form server Progress bar will show to end user
                        loadStateAdapter));
        // set Grid span to set progress at center
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                // If progress will be shown then span size will be 1 otherwise it will be 2
//                return moviesAdapter.getItemViewType(position) == MoviesAdapter.LOADING_ITEM ? 1 : 2;
//            }
//        });
    }
}
