package com.group_1.usege.library.adapter;

import android.location.GnssAntennaInfo;
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
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalByIdReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalByItemReceiver;

import org.jetbrains.annotations.NotNull;

public abstract class ImagesAdapter<S extends ImagesAdapter.ImageViewHolder> extends PagingDataAdapter<Image, S> {
    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int IMAGE_ITEM = 1;
    private OnClickListener mListener;
    private final RequestManager glide;
    private final ViewDetailsSignalByItemReceiver<Image> viewDetailsSignalReceiver;
    public ImagesAdapter(@NotNull DiffUtil.ItemCallback<Image> diffCallback,
                         RequestManager glide,
                         ViewDetailsSignalByItemReceiver<Image> viewDetailsSignalReceiver) {
        super(diffCallback);
        this.glide = glide;
        this.viewDetailsSignalReceiver = viewDetailsSignalReceiver;
    }

    @NonNull
    @Override
    public abstract S onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image currentImg = getItem(position);
        // Check for null
        if (currentImg != null) {
            holder.bind(currentImg, glide, viewDetailsSignalReceiver);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(currentImg, position);
                    }
                }
            });
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
        public void bind(Image img, RequestManager glide, ViewDetailsSignalByItemReceiver<Image> viewDetailsSignalReceiver)
        {
            glide.load(img.getSmallUri())
                    .into(imgView);
            if (viewDetailsSignalReceiver == null)
                return;
            layoutContainer.setOnClickListener(v -> viewDetailsSignalReceiver.view(img));
        }
    }

    public interface OnClickListener {
        void onItemClick(Image image, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }
}
