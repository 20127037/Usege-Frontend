package com.group_1.usege.syncing.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group_1.usege.R;

import java.util.ArrayList;
import java.util.List;

public class ImageCardFragment  extends Fragment implements FragmentCallBacks{
    TextView totalImage;
    RecyclerView rcvPhoto;
    RecycleAdapter recycleAdapter;
    List<Uri> uriList = new ArrayList<>();

    Context context = null;
    public ImageCardFragment() {
        // Required empty public constructor
    }

    public static ImageCardFragment newInstance() {
        ImageCardFragment fragment = new ImageCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        LinearLayout layout_library = (LinearLayout) inflater.inflate(R.layout.fragment_image_card, null);

        totalImage = layout_library.findViewById(R.id.total_image);
        rcvPhoto = layout_library.findViewById(R.id.rcv_photo);

        recycleAdapter = new RecycleAdapter(uriList, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(recycleAdapter);

        return layout_library;
    }


    @Override
    public void receiveUriList(List<Uri> lstUri, int size) {
        uriList.addAll(lstUri);
        totalImage.setText("Images: (" + size + ")");
        recycleAdapter.notifyDataSetChanged();
    }
}
