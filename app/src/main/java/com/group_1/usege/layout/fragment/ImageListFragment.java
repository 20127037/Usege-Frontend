package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group_1.usege.R;
import com.group_1.usege.layout.adapter.RecycleAdapter;
import com.group_1.usege.layout.adapter.RecycleListAdapter;
import com.group_1.usege.modle.Image;

import java.io.Serializable;
import java.util.List;

public class ImageListFragment extends Fragment {
    TextView totalImage;
    RecyclerView rcvPhoto;
    public RecycleAdapter recycleAdapter;
    private List<Image> lstImage;
    private Context context = null;
    public ImageListFragment() {
        // Required empty public constructor
    }

    public static ImageListFragment newInstance(List<Image> images) {
        ImageListFragment fragment = new ImageListFragment();
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

        LinearLayout layoutImageList = (LinearLayout) inflater.inflate(R.layout.fragment_image_list, null);

        totalImage = layoutImageList.findViewById(R.id.total_image);
        rcvPhoto = layoutImageList.findViewById(R.id.rcv_photo);

        recycleAdapter = new RecycleAdapter(lstImage, context, "list");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvPhoto.setLayoutManager(linearLayoutManager);
        rcvPhoto.setAdapter(recycleAdapter);

        return layoutImageList;
    }
}