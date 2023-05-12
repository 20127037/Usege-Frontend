package com.group_1.usege.library.utilities.comparators;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.group_1.usege.model.Image;

public class ImageComparator extends DiffUtil.ItemCallback<Image> {
    @Override
    public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
        return oldItem.getUri().equals(newItem.getUri());
    }
}
