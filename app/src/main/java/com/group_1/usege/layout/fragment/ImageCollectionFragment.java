package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.library.adapter.ImagesAdapter;
import com.group_1.usege.library.paging.PagingProvider;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.library.service.MasterFileServiceGenerator;
import com.group_1.usege.library.utilities.comparators.ImageComparator;
import com.group_1.usege.library.viewModel.UsegeImageViewModel;
import com.group_1.usege.model.Image;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.utilities.adapter.LoadStateAdapter;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalByItemReceiver;
import com.group_1.usege.utilities.modules.ActivityModule;

import java.util.Map;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Single;

public abstract class ImageCollectionFragment<S extends ImagesAdapter.ImageViewHolder> extends Fragment {
    @Inject
    public TokenRepository tokenRepository;
    @Inject
    @ActivityModule.SmallPlaceHolder
    public RequestManager requestManager;
    @Inject
    public ImageComparator comparator;
    @Inject
    public LoadStateAdapter loadStateAdapter;
    @Inject
    public MasterFileServiceGenerator masterFileServiceGenerator;
    private static final int SPAN_COUNT = 3;
    protected final PagingProvider<Map<String, String>, MasterFileService.QueryResponse<UserFile>> defaultProvider = this::paging;
    protected ImagesAdapter<S> imageAdapter;
    protected UsegeImageViewModel mainViewModel;
    protected ViewDetailsSignalByItemReceiver<Image> viewDetailsSignalReceiver;
    private RecyclerView rcvPhoto;
    LibraryActivity libraryActivity;

    public ImageCollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewDetailsSignalReceiver = (ViewDetailsSignalByItemReceiver<Image>) context;
    }


    public abstract ImagesAdapter<S> provideImageAdapter();

    public abstract RecyclerView.LayoutManager provideLayoutManager();

    public abstract @LayoutRes int provideLayout();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //imageAdapter = new SimpleImagesAdapter(comparator, requestManager, viewDetailsSignalReceiver);
        imageAdapter = provideImageAdapter();
        mainViewModel = new ViewModelProvider(this).get(UsegeImageViewModel.class);
        mainViewModel.init(defaultProvider);

        libraryActivity = (LibraryActivity) getActivity();
        imageAdapter.setOnClickListener(new ImagesAdapter.OnClickListener() {
            @Override
            public void onItemClick(Image image, int position) {
                libraryActivity.sendAndReceiveImage(image, position);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutImageList = inflater.inflate(provideLayout(), null);
        rcvPhoto = layoutImageList.findViewById(R.id.rcv_photo);
        //set recyclerview and adapter
        // Subscribe to to paging data
        initRecyclerviewAndAdapter(rcvPhoto);
        mainViewModel.getImagePagingDataFlowable()
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(imagePagingData -> imageAdapter.submitData(getLifecycle(), imagePagingData));
        return layoutImageList;
    }


    public void initRecyclerviewAndAdapter(RecyclerView recyclerView) {
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        recyclerView.setLayoutManager(provideLayoutManager());
        recyclerView.setAdapter(imageAdapter.withLoadStateFooter(loadStateAdapter));
    }

    private Single<MasterFileService.QueryResponse<UserFile>> paging(Map<String, String> page) {
        return masterFileServiceGenerator
                .getService()
                .getFiles(tokenRepository.getToken().getUserId(), false, UsegeImageViewModel.PAGE_SIZE, null, page);
    }
}
