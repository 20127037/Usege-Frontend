package com.group_1.usege.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;

import org.jetbrains.annotations.NotNull;

public class ImageLoadStateAdapter extends LoadStateAdapter<ImageLoadStateAdapter.LoadStateViewHolder> {

    public ImageLoadStateAdapter() {
        super();
    }

    @NotNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup parent,
                                                  @NotNull LoadState loadState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_trash_item, parent, false);
        return new LoadStateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull LoadStateViewHolder holder,
                                 @NotNull LoadState loadState) {
        // Call Bind Method to bind visibility  of views
        holder.bind(loadState);
    }

    public static class LoadStateViewHolder extends RecyclerView.ViewHolder {
        // Define Progress bar
        private final ProgressBar progressBar;
        private final TextView txtError;
        // Define error TextView

        LoadStateViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            txtError = itemView.findViewById(R.id.txt_error);
        }

        public void bind(LoadState loadState) {
            // Check load state
            if (loadState instanceof LoadState.Error) {
                // Get the error
                LoadState.Error loadStateError = (LoadState.Error) loadState;
                // Set text of Error message
                txtError.setText(loadStateError.getError().getLocalizedMessage());
            }
            // set visibility of widgets based on LoadState
            progressBar.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
            txtError.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
        }
    }
}