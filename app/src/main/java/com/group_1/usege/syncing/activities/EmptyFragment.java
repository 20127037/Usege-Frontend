package com.group_1.usege.syncing.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group_1.usege.R;

public class EmptyFragment extends Fragment {

    TextView textViewSync;
    Context context = null;
    LibraryActivity libraryActivity;
    public EmptyFragment() {
        // Required empty public constructor
    }

    public static EmptyFragment newInstance() {
        EmptyFragment fragment = new EmptyFragment();
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

        textViewSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetDialog();
            }
        });

        return layout_empty;
    }

    public void openBottomSheetDialog() {
        libraryActivity.clickOpenSetUpSyncingBottomSheetDialog();
    }
}