package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.group_1.usege.R;
import com.group_1.usege.syncing.activities.LibraryActivity;

public class EmptyFilteringResultFragment extends Fragment {
    Context context = null;
    LibraryActivity libraryActivity;
    public EmptyFilteringResultFragment() {
        // Required empty public constructor
    }

    public static EmptyFilteringResultFragment newInstance() {
        EmptyFilteringResultFragment fragment = new EmptyFilteringResultFragment();
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
        LinearLayout layout_empty = (LinearLayout) inflater.inflate(R.layout.fragment_empty_filtering_result ,null);

        return layout_empty;
    }

}