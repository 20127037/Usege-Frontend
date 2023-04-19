package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.model.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageCardFragment  extends Fragment {
    LibraryActivity libraryActivity;
    public RecyclerView rcvPhoto;

    public CardAdapter cardAdapter;
    private List<Image> listImage;

    private List<Image> favoriteListImage = new ArrayList<Image>();
    private Context context = null;
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

    public static ImageCardFragment newInstance(List<Image> images, List<Image> favoriteImages) {
        ImageCardFragment fragment = new ImageCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_images", (Serializable) images);
        args.putSerializable("List_favorite_images", (Serializable) favoriteImages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listImage = (List<Image>) getArguments().getSerializable("List_images");
            if((List<Image>) getArguments().getSerializable("List_favorite_images") != null) {
                favoriteListImage = new ArrayList<Image>((List<Image>) getArguments().getSerializable("List_favorite_images"));
            }
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

        if(favoriteListImage.size() > 0) {
            RecyclerView rcvFavoritePhoto = layoutImageCard.findViewById(R.id.rcv_favorite_photo);
            View viewDivider = layoutImageCard.findViewById(R.id.view_divider);
            TextView textviewFavorite = layoutImageCard.findViewById(R.id.text_view_favorite);

            rcvFavoritePhoto.setVisibility(View.VISIBLE);
            viewDivider.setVisibility(View.VISIBLE);
            textviewFavorite.setVisibility(View.VISIBLE);

            ArrayList<Image> firstSixItems;
            if(favoriteListImage.size() > 6) {
                firstSixItems = new ArrayList<>(favoriteListImage.subList(0, 6));
            } else {
                firstSixItems = new ArrayList<>(favoriteListImage.subList(0, favoriteListImage.size()));
            }
            CardAdapter favoriteCardAdapter = new CardAdapter(firstSixItems, context, new IClickItemImageListener() {
                @Override
                public void onClickItemImage(Image image, int position) {
                    onClickGoToDetails(image, position);
                }
            });

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            rcvFavoritePhoto.setLayoutManager(gridLayoutManager);
            rcvFavoritePhoto.setAdapter(favoriteCardAdapter);
        }
        cardAdapter = new CardAdapter(listImage, context, new IClickItemImageListener() {
            @Override
            public void onClickItemImage(Image image, int position) {
                onClickGoToDetails(image, position);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(cardAdapter);

        return layoutImageCard;
    }

    private void onClickGoToDetails(Image image, int position) {
//        Intent intent = new Intent(context, ImageActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("object_image", image);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
        libraryActivity.sendAndReceiveImage(image, position);
    }
}
