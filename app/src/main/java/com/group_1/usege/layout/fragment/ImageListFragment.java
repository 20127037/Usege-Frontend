package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.library.adapter.SimpleImagesAdapter;
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
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Single;

@AndroidEntryPoint
public class ImageListFragment extends Fragment {

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
    private static final int LIMIT = 6;
    private final PagingProvider<Map<String,String>, MasterFileService.QueryResponse<UserFile>> defaultProvider = this::paging;
    private SimpleImagesAdapter imageAdapter;
    private UsegeImageViewModel mainViewModel;
    private ViewDetailsSignalByItemReceiver<Image> viewDetailsSignalReceiver;
    private RecyclerView rcvPhoto;
    public ImageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewDetailsSignalReceiver = (ViewDetailsSignalByItemReceiver<Image>)context;
    }

    public static ImageListFragment newInstance() {
        return new ImageListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            imageAdapter = new SimpleImagesAdapter(comparator, requestManager, viewDetailsSignalReceiver);
            mainViewModel = new ViewModelProvider(this).get(UsegeImageViewModel.class);
            mainViewModel.init(defaultProvider);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layoutImageList = (LinearLayout) inflater.inflate(R.layout.fragment_image_list, null);
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(imageAdapter.withLoadStateFooter(loadStateAdapter));
    }

    private Single<MasterFileService.QueryResponse<UserFile>> paging(Map<String, String> page) {
        return masterFileServiceGenerator
                .getService()
                .getFiles(tokenRepository.getToken().getUserId(), false, LIMIT, null, page);
    }
}