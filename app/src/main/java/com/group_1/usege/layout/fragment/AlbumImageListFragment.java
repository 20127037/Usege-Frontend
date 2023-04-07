package com.group_1.usege.layout.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.layout.adapter.RecycleAdapter;
import com.group_1.usege.modle.Album;
import com.group_1.usege.modle.Image;
import com.group_1.usege.syncing.activities.LibraryActivity;

import java.io.Serializable;
import java.util.List;

public class AlbumImageListFragment extends Fragment  {
    TextView totalImage;

    public RecyclerView rcvPhoto;

    public RecycleAdapter recycleAdapter;
    private Album album;
    private Context context = null;
    public AlbumImageListFragment() {
        // Required empty public constructor
    }

    public static AlbumImageListFragment newInstance(Album album) {
        AlbumImageListFragment fragment = new AlbumImageListFragment();
        Bundle args = new Bundle();
        args.putSerializable("album", (Serializable) album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable("album");
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

        LinearLayout layoutImageList = (LinearLayout) inflater.inflate(R.layout.fragment_image_list_album, null);


        rcvPhoto = layoutImageList.findViewById(R.id.rcv_photo);
        TextView albumName = layoutImageList.findViewById(R.id.text_view_album_name);
        TextView albumSubtitle = layoutImageList.findViewById(R.id.text_view_album_sub_title);
        ImageView backImageView = layoutImageList.findViewById(R.id.image_view_backward);
        albumName.setText(album.getName());
        albumSubtitle.setText(String.format("%d images", album.getAlbumImages().size()));
        backImageView.setOnClickListener(v -> {
            if (context.getClass().equals(LibraryActivity.class)) {
                Activity activity = (Activity) context;
                if (activity instanceof LibraryActivity) {
                    LibraryActivity libActivity = (LibraryActivity) activity;
                    libActivity.triggerAlbumButton();
                }
            }
        });

        recycleAdapter = new RecycleAdapter(album.getAlbumImages(), context, "list");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvPhoto.setLayoutManager(linearLayoutManager);
        rcvPhoto.setAdapter(recycleAdapter);

        return layoutImageList;
    }
}