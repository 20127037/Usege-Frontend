package com.group_1.usege.library.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.model.Image;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalReceiver;

import java.util.List;


public class ExternalImageListAdapter extends RecyclerView.Adapter<ExternalImageListAdapter.ImageViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private List<Image> lstImage;
    private Context context;
    private ViewDetailsSignalReceiver viewDetailsSignalReceiver;
    private Boolean isLoadingAdd = false;


    public ExternalImageListAdapter(Context context, ViewDetailsSignalReceiver viewDetailsSignalReceiver) {
        this.context = context;
        this.viewDetailsSignalReceiver = viewDetailsSignalReceiver;
    }

    public void setData(List<Image> images) {
        this.lstImage = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (lstImage != null && position == lstImage.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }

        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            // Load ảnh ra layout
            Image image = lstImage.get(position);

            Uri uri = image.getUri();
            Glide.with(context)
                    .load(uri)
                    .into(holder.imgView);
            Log.d("SIZE", "I: " + uri);

            holder.imgView.setOnClickListener(v -> {
                if (viewDetailsSignalReceiver != null)
                    viewDetailsSignalReceiver.view(image.getId());
            });

        }

    }

    @Override
    public int getItemCount() {
        if (lstImage != null) {
            return lstImage.size();
        }

        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        CardView layoutItemCard;
        ImageView imgView;
        TextView photoText;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.image_view_photo);
            layoutItemCard = itemView.findViewById(R.id.layout);
            photoText = itemView.findViewById(R.id.text_view_photo_text);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        lstImage.add(new Image());
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int position = lstImage.size() - 1;
        Image image = lstImage.get(position);
        if (image != null) {
            lstImage.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void release() {
        context = null;
    }
}

