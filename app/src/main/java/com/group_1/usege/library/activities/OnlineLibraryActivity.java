package com.group_1.usege.library.activities;

import static com.group_1.usege.library.viewModel.PexelsLibraryImageViewModel.PAGE_SIZE;

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
import com.group_1.usege.library.adapter.ImagesAdapter;
import com.group_1.usege.library.adapter.SimpleImagesAdapter;
import com.group_1.usege.library.model.PexelsPageResponse;
import com.group_1.usege.library.paging.PagingProvider;
import com.group_1.usege.library.service.PexelsServiceGenerator;
import com.group_1.usege.library.utilities.comparators.ImageComparator;
import com.group_1.usege.library.viewModel.PexelsLibraryImageViewModel;
import com.group_1.usege.model.Image;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.AuthApiCallerActivity;
import com.group_1.usege.utilities.activities.NavigatedAuthApiCallerActivity;
import com.group_1.usege.utilities.adapter.LoadStateAdapter;
import com.group_1.usege.utilities.modules.ActivityModule;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnlineLibraryActivity extends NavigatedAuthApiCallerActivity<Void> {
    public static final int SPAN_COUNT = 3;
    @Inject
    public PexelsServiceGenerator pexelsServiceGenerator;
    private final PagingProvider<Integer, PexelsPageResponse> defaultProvider = page -> pexelsServiceGenerator
            .getService(R.string.key_pexels_api)
            .searchPage(page, PAGE_SIZE);
    private String currentQuery = "";
    private PagingProvider<Integer, PexelsPageResponse> getSearchProvider(String search){
        return page -> pexelsServiceGenerator
                .getService(R.string.key_pexels_api)
                .searchPage(search, page, PAGE_SIZE);
    }

    @Inject
    @ActivityModule.SmallPlaceHolder
    public RequestManager requestManager;
    @Inject
    public ImageComparator comparator;
    @Inject
    public LoadStateAdapter loadStateAdapter;
    private SimpleImagesAdapter imageAdapter;
    private PexelsLibraryImageViewModel mainViewModel;

    public OnlineLibraryActivity()
    {
        super(R.layout.activity_online_library);
    }

    @Override
    public int navigateId() {
        return R.id.nav_external_library;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SearchView searchView = findViewById(R.id.view_search);
        searchView.setOnCloseListener(() -> {
            afterSearchSubmit(searchView.getQuery().toString());
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                afterSearchSubmit(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lst_img);

        Lifecycle lifecycle = getLifecycle();

        // Create new MoviesAdapter object and provide
        imageAdapter = new SimpleImagesAdapter(comparator, requestManager, this::viewImg, null);
        mainViewModel = new ViewModelProvider(this).get(PexelsLibraryImageViewModel.class);
        mainViewModel.setCurrentProvider(defaultProvider);
        mainViewModel.init();
        //set recyclerview and adapter
        initRecyclerviewAndAdapter(recyclerView);
        // Subscribe to to paging data
        mainViewModel.getImagePagingDataFlowable()
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle)))
                .subscribe(imagePagingData -> imageAdapter.submitData(lifecycle, imagePagingData));
    }

    private void afterSearchSubmit(String query)
    {
        Log.i("Old query", currentQuery);
        Log.i("New query", query);
        if (query.equals(currentQuery))
            return;
        currentQuery = query;
        refreshPaging();
    }
    private void refreshPaging()
    {
        PagingProvider<Integer, PexelsPageResponse> provider =
                currentQuery.isEmpty() ? defaultProvider : getSearchProvider(currentQuery);
        mainViewModel.setCurrentProvider(provider);
        imageAdapter.refresh();
    }

    private void viewImg(Image img, ImagesAdapter.ImageViewHolder viewHolder, int pos)
    {
        Bundle transBundle = new Bundle();
        transBundle.putParcelable(OnlineImageActivity.IMG_KEY, img);
        ActivityUtilities.TransitActivity(this, OnlineImageActivity.class, transBundle);
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

    @Override
    protected void handleCallSuccess(Void body) {

    }
}
