package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.group_1.usege.R;
import com.group_1.usege.model.Image;
import com.group_1.usege.layout.adapter.ListAdapter;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.pagination.PaginationScrollListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends Fragment {
    LibraryActivity libraryActivity;
    public int position;

    public RecyclerView rcvPhoto;

    public ListAdapter listAdapter;
    private List<Image> lstImage;
    private List<Image> lstVisibleImage;
    private Context context = null;
    private Boolean isLoading = false;
    private Boolean isLastPage = false;
    private int totalPage;
    private int currentPage = 1;

    private static final int countItemInPage = 5;
    public ImageListFragment() {
        // Required empty public constructor
    }

    public static ImageListFragment newInstance(List<Image> images) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_images", (Serializable) images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            lstImage = (List<Image>) getArguments().getSerializable("List_images");
            totalPage = lstImage.size() / countItemInPage + 1;
        }

        try {
            context = getActivity();
            libraryActivity = (LibraryActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layoutImageList = (LinearLayout) inflater.inflate(R.layout.fragment_image_list, null);

        rcvPhoto = layoutImageList.findViewById(R.id.rcv_photo);

        listAdapter = new ListAdapter(context, new IClickItemImageListener() {
            @Override
            public void onClickItemImage(Image image, int position) {
                onClickGoToDetails(image, position);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvPhoto.setLayoutManager(linearLayoutManager);
        rcvPhoto.setAdapter(listAdapter);

        setFirstData();
        rcvPhoto.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                isLoading = true;

                currentPage += 1;
                loadNextPage();
            }

            @Override
            public Boolean isLoading() {
                return isLoading;
            }

            @Override
            public Boolean isLastPage() {
                return isLastPage;
            }
        });

        return layoutImageList;
    }


    private void onClickGoToDetails(Image image, int position) {
        libraryActivity.sendAndReceiveImage(image, position);
    }

    /**
     * Load data page 1
     */
    private void setFirstData() {
        position = 0;
        lstVisibleImage = getListImage();
        listAdapter.setData(lstVisibleImage);

        if (currentPage < totalPage) {
            listAdapter.addFooterLoading();
            isLastPage = false;
        } else {
            isLastPage = true;
        }
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Image> images = getListImage();

                listAdapter.removeFooterLoading();
                lstVisibleImage.addAll(images);
                listAdapter.notifyDataSetChanged();

                isLoading = false;
                Log.e("Page", "Current " + currentPage);
                if (currentPage < totalPage) {
                    listAdapter.addFooterLoading();
                    isLastPage = false;
                } else {
                    isLastPage = true;
                }
            }
        }, 750);

    }

    private List<Image> getListImage() {
        Toast.makeText(context, "Load data page " + currentPage, Toast.LENGTH_LONG).show();

        List<Image> images = new ArrayList<>();

        for (int i = 0; i < countItemInPage; i++) {
            if (position <= lstImage.size() - 1) {
                images.add(lstImage.get(position));
                position += 1;
            }
        }

        return images;
    }
}