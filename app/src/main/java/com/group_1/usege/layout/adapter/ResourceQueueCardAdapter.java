package com.group_1.usege.layout.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ResourceQueueCardViewHolder holder, int position) {
        Image image = resourceQueueImageList.get(position);
        if (image == null) return;
        Uri uri = image.getUri();
        Glide.with(context).load(uri).into(holder.resourceQueueImageView);

        holder.resourceQueueImageView.setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(uri);
            ClipData dragData = new ClipData("RESOURE_QUEUE_IMAGE_VIEW", new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(v);
            v.startDragAndDrop(dragData, myShadow, v, 0);
            v.setVisibility(View.VISIBLE);
            return true;
        });

        holder.resourceQueueImageView.setOnClickListener(v -> {
            v.setVisibility(View.GONE);
        });

    }

    @Override
    public int getItemCount() {
        if (resourceQueueImageList != null) {
            return resourceQueueImageList.size();
        }
        return 0;
    }

    public class ResourceQueueCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView resourceQueueImageView;
        public ResourceQueueCardViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceQueueImageView = itemView.findViewById(R.id.resource_queue_image_view);
        }
    }
}
