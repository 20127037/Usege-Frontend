package com.group_1.usege.syncing.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.group_1.usege.R;
import com.group_1.usege.syncing.activities.LibraryActivity;

public class EmptyAlbumImageFragment extends Fragment {

    TextView textViewSync;
    Context context = null;
    LibraryActivity libraryActivity;
    public EmptyAlbumImageFragment() {
        // Required empty public constructor
    }

    public static EmptyAlbumImageFragment newInstance() {
        EmptyAlbumImageFragment fragment = new EmptyAlbumImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        textViewSync = layout_empty.findViewById(R.id.text_view_sync);

        textViewSync.setText("Let's add some image to Album!");


        textViewSync.setVisibility(View.GONE);

        textViewSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetDialog();
            }
        });

        return layout_empty;
    }

    public void openBottomSheetDialog() {
//        libraryActivity.clickAddImageToAlbum();
    }
}