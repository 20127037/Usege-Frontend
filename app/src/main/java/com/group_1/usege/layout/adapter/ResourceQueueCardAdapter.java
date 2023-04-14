package com.group_1.usege.layout.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.model.Image;

import java.util.List;

public class ResourceQueueCardAdapter extends RecyclerView.Adapter<ResourceQueueCardAdapter.ResourceQueueCardViewHolder> {
    Context context;
    private List<Image> resourceQueueImageList;

    public ResourceQueueCardAdapter(List<Image> list, Context context, IClickItemImageListener iClickItemImageListener) {
        this.resourceQueueImageList = list;
        this.context = context;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ResourceQueueCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource_queue_image, parent, false);
        return new ResourceQueueCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceQueueCardViewHolder holder, int position) {
        Image image = resourceQueueImageList.get(position);
        if (image == null) {
            return;
        }
        Uri uri = image.getUri();

        Glide.with(context)
                .load(uri)
                .into(holder.resourceQueueImage);
    }

    @Override
    public int getItemCount() {
        if (resourceQueueImageList != null) {
            return resourceQueueImageList.size();
        }
        return 0;
    }

    public class ResourceQueueCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView resourceQueueImage;
        public ResourceQueueCardViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceQueueImage = itemView.findViewById(R.id.resource_queue_image_view);

        }
    }
}
