package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.layout.adapter.ResourceQueueCardAdapter;
import com.group_1.usege.library.activities.ImageCombinationActivity;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.model.Image;

import java.io.Serializable;
import java.util.List;

public class ImageResourceQueueCardFragment extends Fragment {
    ImageCombinationActivity imageCombinationActivity;

    public RecyclerView resourceQueueRecyclerView;

    public ResourceQueueCardAdapter resourceQueueCardAdapter;
    private List<Image> resourceQueueImageList;
    private Context context = null;
    public ImageResourceQueueCardFragment() {
        // Required empty public constructor
    }

    public static ImageResourceQueueCardFragment newInstance(List<Image> images) {
        ImageResourceQueueCardFragment fragment = new ImageResourceQueueCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_images", (Serializable) images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            resourceQueueImageList = (List<Image>) getArguments().getSerializable("List_images");
        }

        try {
            context = getActivity();
            imageCombinationActivity = (ImageCombinationActivity) context;
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View imageCardLayout = inflater.inflate(R.layout.fragment_resource_queue_image_card, null);

        resourceQueueRecyclerView = imageCardLayout.findViewById(R.id.resource_queue_recycler_view);

        resourceQueueCardAdapter = new ResourceQueueCardAdapter(resourceQueueImageList, context, new IClickItemImageListener() {
            @Override
            public void onClickItemImage(Image image, int position) {
                return;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        resourceQueueRecyclerView.setLayoutManager(linearLayoutManager);
        resourceQueueRecyclerView.setAdapter(resourceQueueCardAdapter);

        return imageCardLayout;
    }

}
