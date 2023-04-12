package com.group_1.usege.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group_1.usege.R;
import com.group_1.usege.model.Album;
import com.group_1.usege.library.activities.LibraryActivity;

import java.io.Serializable;

public class EmptyFragment extends Fragment {
    Context context = null;
    LibraryActivity libraryActivity;
    String mode;
    boolean showBack;
    public EmptyFragment() {
        // Required empty public constructor
    }

    public static EmptyFragment newInstance(String mode, boolean showBack) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();

        args.putSerializable("mode", (Serializable) mode);
        args.putSerializable("showBack", (Serializable) showBack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showBack = (boolean) getArguments().getSerializable("showBack");
            mode = (String) getArguments().getSerializable("mode");
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
        LinearLayout layout_empty = (LinearLayout) inflater.inflate(R.layout.fragment_empty, null);
        TextView textViewSync;
        ImageView ImgViewBack;
        textViewSync = layout_empty.findViewById(R.id.text_view_sync);
        ImgViewBack = layout_empty.findViewById(R.id.image_view_backward);

        switch (mode) {
            case LibraryActivity.imageMode:
//                System.out.println("IMAGE 1");
                ImgViewBack.setVisibility(View.GONE);
                textViewSync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openBottomSheetDialog();
                    }
                });
                break;
            case LibraryActivity.albumMode:
//                System.out.println("IMAGE 2");
                ImgViewBack.setVisibility(View.VISIBLE);
                textViewSync.setVisibility(View.GONE);
                ImgViewBack.setOnClickListener(v -> {
                    System.out.println("Clicked!");
                    if (context.getClass().equals(LibraryActivity.class)) {
                        Activity activity = (Activity) context;
                        if (activity instanceof LibraryActivity) {
                            System.out.println("Clicked zo!");
                            LibraryActivity libActivity = (LibraryActivity) activity;
                            libActivity.triggerAlbumButton();
                            libActivity.setShowLayoutLibFuntions();
                        }
                    }
                });
                break;
            case LibraryActivity.imageInAlbumMode:
//                System.out.println("IMAGE 3");
                ImgViewBack.setVisibility(View.VISIBLE);
                textViewSync.setVisibility(View.GONE);
                ImgViewBack.setOnClickListener(v -> {
                    if (context.getClass().equals(LibraryActivity.class)) {
                        Activity activity = (Activity) context;
                        if (activity instanceof LibraryActivity) {
                            LibraryActivity libActivity = (LibraryActivity) activity;
                            libActivity.triggerAlbumButton();
                            libActivity.setShowLayoutLibFuntions();
                        }
                    }
                });
                break;
        }

        return layout_empty;
    }

    public void openBottomSheetDialog() {
        libraryActivity.clickOpenSetUplibraryBottomSheetDialog();
    }
}