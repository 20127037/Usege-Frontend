package com.group_1.usege.utilities.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.library.adapter.ImagesAdapter;

public interface ClickItemReceiver<S, H extends RecyclerView.ViewHolder> {
    void view(S item, H viewHolder, int pos);
}
