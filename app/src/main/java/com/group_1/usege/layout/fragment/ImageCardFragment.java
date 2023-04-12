package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.layout.adapter.RecycleAdapter;
import com.group_1.usege.model.Image;

import java.io.Serializable;
import java.util.List;

public class ImageCardFragment  extends Fragment {
    FragmentTransaction ft;
    TextView totalImage;

    public RecyclerView rcvPhoto;

    public RecycleAdapter recycleAdapter;
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

        recycleAdapter = new RecycleAdapter(lstImage, context, "card");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(recycleAdapter);

        return layoutImageCard;
    }


}
