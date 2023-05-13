package com.group_1.usege.utilities.interfaces;

import androidx.recyclerview.widget.RecyclerView;

public interface LongClickItemReceiver<S, H extends RecyclerView.ViewHolder> {
    void longView(S item, H viewHolder, int pos);
}
