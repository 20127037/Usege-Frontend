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

import com.group_1.usege.R;
import com.group_1.usege.layout.adapter.CardAdapter;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.pagination.PaginationScrollListener;
import com.group_1.usege.model.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageCardFragment  extends Fragment {
    LibraryActivity libraryActivity;
    public int position;
    public RecyclerView rcvPhoto;

    public CardAdapter cardAdapter;
    private List<Image> lstImage;
    private List<Image> lstVisibleImage;
    private Context context = null;
    private Boolean isLoading = false;
    private Boolean isLastPage = false;
    private int totalPage;
    private int currentPage = 1;

    private static final int countItemInPage = 12;
    public ImageCardFragment() {
        // Required empty public constructor
    }

    public static ImageCardFragment newInstance(List<Image> images) {
        ImageCardFragment fragment = new ImageCardFragment();
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
            if (context instanceof LibraryActivity) {
                libraryActivity = (LibraryActivity) context;
            }
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutImageCard = inflater.inflate(R.layout.fragment_image_card, null);

        rcvPhoto = layoutImageCard.findViewById(R.id.rcv_photo);

        cardAdapter = new CardAdapter(context, new IClickItemImageListener() {
            @Override
            public void onClickItemImage(Image image, int position) {
                onClickGoToDetails(image, position);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(cardAdapter);

        setFirstData();
        rcvPhoto.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
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

        return layoutImageCard;
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
        cardAdapter.setData(lstVisibleImage);

        if (currentPage < totalPage) {
            cardAdapter.addFooterLoading();
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

                cardAdapter.removeFooterLoading();
                lstVisibleImage.addAll(images);
                cardAdapter.notifyDataSetChanged();

                isLoading = false;
                Log.e("Page", "Current " + currentPage);
                if (currentPage < totalPage) {
                    cardAdapter.addFooterLoading();
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
