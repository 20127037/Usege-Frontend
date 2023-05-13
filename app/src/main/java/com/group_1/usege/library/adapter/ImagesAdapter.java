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
import com.group_1.usege.utilities.interfaces.ClickItemReceiver;
import com.group_1.usege.utilities.interfaces.LongClickItemReceiver;

import org.jetbrains.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

public abstract class ImagesAdapter<S extends ImagesAdapter.ImageViewHolder> extends PagingDataAdapter<Image, S> {
    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int IMAGE_ITEM = 1;
    private final RequestManager glide;
    private final ClickItemReceiver<Image, ImageViewHolder> clickItemReceiver;
    private final LongClickItemReceiver<Image, ImageViewHolder> longClickItemReceiver;
    public ImagesAdapter(@NotNull DiffUtil.ItemCallback<Image> diffCallback,
                         RequestManager glide,
                         ClickItemReceiver<Image, ImageViewHolder> clickItemReceiver,
                         LongClickItemReceiver<Image, ImageViewHolder> longClickItemReceiver) {
        super(diffCallback);
        this.glide = glide;
        this.clickItemReceiver = clickItemReceiver;
        this.longClickItemReceiver = longClickItemReceiver;
    }

    @NonNull
    @Override
    public abstract S onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull S holder, int position) {
        Image currentImg = getItem(position);
        // Check for null
        if (currentImg != null) {
            holder.bind(currentImg, glide, clickItemReceiver, longClickItemReceiver, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // set ViewType
        return position == getItemCount() ? IMAGE_ITEM : LOADING_ITEM;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        // Define movie_item layout view binding
        protected final ImageView imgView;
        protected  ViewGroup layoutContainer;


        public ImageViewHolder(@NonNull View view) {
            super(view);
            layoutContainer = view.findViewById(R.id.layout);
            // init binding
            imgView = view.findViewById(R.id.image_view_photo);
        }
        public void bind(Image img, RequestManager glide, ClickItemReceiver<Image, ImageViewHolder> clickItemReceiver,
                         LongClickItemReceiver<Image, ImageViewHolder> longClickItemReceiver, int position)
        {
            glide.load(img.getSmallUri())
                    .into(imgView);
            if (clickItemReceiver != null)
                layoutContainer.setOnClickListener(v -> clickItemReceiver.view(img, this, position));
            if (longClickItemReceiver != null)
                layoutContainer.setOnLongClickListener(v -> {
                    longClickItemReceiver.longView(img, this, position);
                    return true;
                });
        }

        public ImageView getImgView() {
            return imgView;
        }

        public ViewGroup getLayoutContainer() {
            return layoutContainer;
        }
    }
}
