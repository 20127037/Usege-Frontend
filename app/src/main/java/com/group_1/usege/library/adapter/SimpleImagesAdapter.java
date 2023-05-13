package com.group_1.usege.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.RequestManager;
import com.group_1.usege.R;
import com.group_1.usege.model.Image;
import com.group_1.usege.utilities.interfaces.ClickItemReceiver;
import com.group_1.usege.utilities.interfaces.LongClickItemReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalByIdReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalByItemReceiver;

/**
 * Show the image but nothing
 */
public class SimpleImagesAdapter extends ImagesAdapter<ImagesAdapter.ImageViewHolder> {
    public SimpleImagesAdapter(@NonNull DiffUtil.ItemCallback<Image> diffCallback,
                               RequestManager glide,
                               ClickItemReceiver<Image, ImagesAdapter.ImageViewHolder> viewDetailsSignalReceiver,
                               LongClickItemReceiver<Image, ImagesAdapter.ImageViewHolder> longClickItemReceiver) {
        super(diffCallback, glide, viewDetailsSignalReceiver, longClickItemReceiver);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View photoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ImageViewHolder(photoView);
    }
}
