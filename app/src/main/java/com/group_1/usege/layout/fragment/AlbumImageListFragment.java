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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;
import com.group_1.usege.layout.adapter.CardAdapter;
import com.group_1.usege.layout.adapter.ListAdapter;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.library.activities.LibraryActivity;

import java.io.Serializable;

public class AlbumImageListFragment extends Fragment {
    LibraryActivity libraryActivity;

    public RecyclerView rcvPhoto;
    public CardAdapter cardAdapter;
    public ListAdapter listAdapter;
    private Album album;
    private String mode;
    private Context context = null;

    public AlbumImageListFragment() {
        // Required empty public constructor
    }

    public static AlbumImageListFragment newInstance(Album album, String mode) {
        AlbumImageListFragment fragment = new AlbumImageListFragment();
        Bundle args = new Bundle();
        args.putSerializable("album", (Serializable) album);
        args.putSerializable("album_mode", (Serializable) mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable("album");
            mode = (String) getArguments().getSerializable("album_mode");
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
        LinearLayout layoutListTitle = layoutImageList.findViewById(R.id.layout_list_title);
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
                    libActivity.setShowLayoutLibFuntions();
                }
            }
        });

        //recycleAdapter = new RecycleAdapter(album.getAlbumImages(), context, mode);
        if (mode == "list") {
            listAdapter = new ListAdapter(album.getAlbumImages(), context, new IClickItemImageListener() {
                @Override
                public void onClickItemImage(Image image, int position) {
                    onClickGoToDetails(image, position);
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            rcvPhoto.setLayoutManager(linearLayoutManager);
            rcvPhoto.setAdapter(listAdapter);
        } else if (mode == "card") {
            layoutListTitle.setVisibility(View.GONE);
            cardAdapter = new CardAdapter(album.getAlbumImages(), context, new IClickItemImageListener() {
                @Override
                public void onClickItemImage(Image image, int position) {
                    onClickGoToDetails(image, position);
                }
            });
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            rcvPhoto.setLayoutManager(gridLayoutManager);
            rcvPhoto.setAdapter(cardAdapter);
            if (context.getClass().equals(LibraryActivity.class)) {
                Activity activity = (Activity) context;
                if (activity instanceof LibraryActivity) {
                    LibraryActivity libActivity = (LibraryActivity) activity;
                    libActivity.moveToAlbum.setOnClickListener(v -> {
                        System.out.print("clicked move!");
                        libActivity.moveToAlbum(album);
                    });
                }
            }
        }

        return layoutImageList;

    }

    private void onClickGoToDetails(Image image, int position) {
        libraryActivity.sendAndReceiveImage(image, position);
    }
}