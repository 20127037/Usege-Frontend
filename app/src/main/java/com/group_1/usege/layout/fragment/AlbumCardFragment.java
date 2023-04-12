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
import com.group_1.usege.layout.adapter.AlbumAdapter;
import com.group_1.usege.modle.Album;

import java.io.Serializable;
import java.util.List;

public class AlbumCardFragment extends Fragment {
    FragmentTransaction ft;
    TextView totalImage;

    public RecyclerView rcvPhoto;

    public AlbumAdapter albumAdapter;
    private List<Album> lstImage;
    private Context context = null;
    public AlbumCardFragment() {
        // Required empty public constructor
    }

    public static AlbumCardFragment newInstance(List<Album> images) {
        AlbumCardFragment fragment = new AlbumCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_album_images", (Serializable) images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            lstImage = (List<Album>) getArguments().getSerializable("List_album_images");
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

        albumAdapter = new AlbumAdapter(lstImage, context, "card");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(albumAdapter);

        return layoutImageCard;
    }
}
