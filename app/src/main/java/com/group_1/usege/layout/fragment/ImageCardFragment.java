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
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.pagination.PaginationScrollListener;
import com.group_1.usege.model.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageCardFragment  extends ImageCollectionFragment<ImagesAdapter.ImageViewHolder> {

    private static final int SPAN_COUNT = 3;
    public ImageCardFragment() {
        // Required empty public constructor
    }
    public static ImageCardFragment newInstance() {
        return new ImageCardFragment();
    }


    @Override
    public ImagesAdapter<ImagesAdapter.ImageViewHolder> provideImageAdapter() {
        return new SimpleImagesAdapter(comparator, requestManager, viewDetailsSignalReceiver);
    }

    @Override
    public RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(getContext(), SPAN_COUNT);
    }

    @Override
    public int provideLayout() {
        return R.layout.fragment_image_card;
    }
}
