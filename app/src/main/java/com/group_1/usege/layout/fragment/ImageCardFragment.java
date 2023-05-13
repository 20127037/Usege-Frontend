package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group_1.usege.R;
import com.group_1.usege.layout.adapter.CardAdapter;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.library.adapter.ImagesAdapter;
import com.group_1.usege.library.adapter.SimpleImagesAdapter;
import com.group_1.usege.library.paging.PagingProvider;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.library.viewModel.FavUsegeImageViewModel;
import com.group_1.usege.library.viewModel.UsegeImageViewModel;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.pagination.PaginationScrollListener;
import com.group_1.usege.model.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Single;

@AndroidEntryPoint
public class ImageCardFragment  extends ImageCollectionFragment<ImagesAdapter.ImageViewHolder> {
    protected ImagesAdapter<ImagesAdapter.ImageViewHolder> favAdapter;
    protected FavUsegeImageViewModel favViewModel;
    protected final PagingProvider<Map<String, String>, MasterFileService.QueryResponse<UserFile>> favProvider = this::favPaging;
    private static final int SPAN_COUNT = 3;
    public ImageCardFragment() {
        // Required empty public constructor
    }
    public static ImageCardFragment newInstance() {
        return new ImageCardFragment();
    }


    @Override
    public ImagesAdapter<ImagesAdapter.ImageViewHolder> provideImageAdapter() {
        return new SimpleImagesAdapter(comparator, requestManager, clickItemReceiver, longClickItemReceiver);
    }

    @Override
    public RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(getContext(), SPAN_COUNT);
    }

    @Override
    public int provideLayout() {
        return R.layout.fragment_image_card;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //imageAdapter = new SimpleImagesAdapter(comparator, requestManager, viewDetailsSignalReceiver);
        favAdapter = provideImageAdapter();
        favViewModel = new ViewModelProvider(this).get(FavUsegeImageViewModel.class);
        favViewModel.init(favProvider);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutImageList = super.onCreateView(inflater, container, savedInstanceState);
        attachViewModelToAdapter(layoutImageList, R.id.rcv_favorite_photo, favViewModel, favAdapter);
        return layoutImageList;
    }

    private Single<MasterFileService.QueryResponse<UserFile>> favPaging(Map<String, String> page) {
        return masterFileServiceGenerator
                .getService()
                .getFiles(tokenRepository.getToken().getUserId(), true, UsegeImageViewModel.PAGE_SIZE, null, page);
    }
}
