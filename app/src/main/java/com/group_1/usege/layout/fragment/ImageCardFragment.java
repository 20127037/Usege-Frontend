package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group_1.usege.R;
import com.group_1.usege.layout.adapter.CardAdapter;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.modle.Image;
import com.group_1.usege.syncing.activities.LibraryActivity;

import java.io.Serializable;
import java.util.List;

public class ImageCardFragment  extends Fragment {
    LibraryActivity libraryActivity;

    public RecyclerView rcvPhoto;

    public CardAdapter cardAdapter;
    private List<Image> lstImage;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            lstImage = (List<Image>) getArguments().getSerializable("List_images");
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

        View layoutImageCard = inflater.inflate(R.layout.fragment_image_card, null);

        rcvPhoto = layoutImageCard.findViewById(R.id.rcv_photo);

        cardAdapter = new CardAdapter(lstImage, context, new IClickItemImageListener() {
            @Override
            public void onClickItemImage(Image image, int position) {
                onClickGoToDetails(image, position);
            }

            @Override
            public void onLongClickItemImage(Image image) {

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
