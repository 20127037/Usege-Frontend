package com.group_1.usege.library.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.group_1.usege.R;
import com.group_1.usege.model.Image;

import org.jetbrains.annotations.NotNull;

public abstract class BaseImagesAdapter<S extends BaseImagesAdapter.ImageViewHolder> extends PagingDataAdapter<Image, S> {
    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int IMAGE_ITEM = 1;
    RequestManager glide;
    public BaseImagesAdapter(@NotNull DiffUtil.ItemCallback<Image> diffCallback, RequestManager glide) {
        super(diffCallback);
        this.glide = glide;
    }

    @NonNull
    @Override
    public abstract S onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Get current movie
        Image currentImg = getItem(position);
        // Check for null
        if (currentImg != null) {
            holder.bind(currentImg, glide);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // set ViewType
        return position == getItemCount() ? IMAGE_ITEM : LOADING_ITEM;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        // Define movie_item layout view binding
        private final ImageView imgView;

        public ImageViewHolder(@NonNull View view) {
            super(view);
            // init binding
            imgView = view.findViewById(R.id.image_view_photo);
        }
        public void bind(Image img, RequestManager glide)
        {
            glide.load(img.getUri())
                    .into(imgView);
        }
    }
}
