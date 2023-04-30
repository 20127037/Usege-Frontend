package com.group_1.usege.library.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Lifecycle;
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
        searchView.setOnSearchClickListener(v -> Log.i("Click", "Search view"));
        RecyclerView recyclerView = findViewById(R.id.lst_img);

        Lifecycle lifecycle = getLifecycle();

        // Create new MoviesAdapter object and provide
        imageAdapter = new SimpleImagesAdapter(comparator, requestManager, null);
        PexelsLibraryImageViewModel mainViewModel = new ViewModelProvider(this).get(PexelsLibraryImageViewModel.class);

        //set recyclerview and adapter
        initRecyclerviewAndAdapter(recyclerView);

        // Subscribe to to paging data
        mainViewModel.getImagePagingDataFlowable()
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle)))
                .subscribe(imagePagingData -> imageAdapter.submitData(lifecycle, imagePagingData));
    }

    private void initRecyclerviewAndAdapter(RecyclerView recyclerView) {
        // Create GridlayoutManger with span of count of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        // Finally set LayoutManger to recyclerview
        recyclerView.setLayoutManager(gridLayoutManager);
        // Add ItemDecoration to add space between recyclerview items
        //recyclerView.addItemDecoration(new GridSpaceDecorator(SPAN_COUNT, 20, true));
        recyclerView.setAdapter(imageAdapter.withLoadStateFooter(loadStateAdapter));
    }
}
