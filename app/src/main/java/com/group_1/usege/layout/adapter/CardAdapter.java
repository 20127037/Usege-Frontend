package com.group_1.usege.layout.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.GlideAppModule;
import com.group_1.usege.R;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Image> lstImage;
    private Context context;
    private IClickItemImageListener iClickItemImageListener;

    private String albumMode = Album.album_mode_default;

    public CardAdapter(List<Image> lstImage, Context context, IClickItemImageListener listener, String albumMode) {
        this.lstImage = lstImage;
        this.context = context;
        this.iClickItemImageListener = listener;
        this.albumMode = albumMode;
    }

    public CardAdapter(List<Image> lstImage, Context context, IClickItemImageListener listener) {
        this.lstImage = lstImage;
        this.context = context;
        this.iClickItemImageListener = listener;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        view = inflater.inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        final int finalPosition = position;
        // Load ảnh ra layout
        Image image = lstImage.get(finalPosition);

        Uri uri = image.getUri();
        Glide.with(context)
                .load(uri)
                .into(holder.imgView);
        Log.d("SIZE", "I: " + uri);

        switch (albumMode) {
            case Album.album_mode_default:
                holder.photoText.setVisibility(View.GONE);
                break;
            case Album.album_mode_trash:
                holder.photoText.setVisibility(View.VISIBLE);
                break;
            case Album.album_mode_favorite:
                holder.photoText.setVisibility(View.GONE);
                break;
            default:
                holder.photoText.setVisibility(View.GONE);
        }

        holder.imgView.setOnClickListener(v -> {
            if (context.getClass().equals(LibraryActivity.class)) {
                Activity activity = (Activity) context;
                if (activity instanceof LibraryActivity) {
                    LibraryActivity libActivity = (LibraryActivity) activity;
                    ImageView imageView = (ImageView) v;
                    if (imageView.getColorFilter() != null) {
                        // FOR UI
                        imageView.clearColorFilter();
                        // FOR LOGIC
                        libActivity.removeSingleImageAndRemoveBottomMenuIfNoImageLeft(image);
                    } else {
                        iClickItemImageListener.onClickItemImage(image, finalPosition);
                    }
                }
            }
        });

        holder.imgView.setOnLongClickListener(v -> {
            if (context.getClass().equals(LibraryActivity.class)) {
                Activity activity = (Activity) context;
                if (activity instanceof LibraryActivity) {
                    LibraryActivity libActivity = (LibraryActivity) activity;
                    // FOR UI
                    ImageView imageView = (ImageView)v;
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.chosen_image));
                    // FOR LOGIC
                    libActivity.selectSingleImageAndOpenBottomMenuIfNotYet(image);
                }
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (lstImage != null) {
            return lstImage.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView layoutItemCard;
        ImageView imgView;
        TextView photoText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_photo);
            layoutItemCard = itemView.findViewById(R.id.layout_item_card);
            photoText = itemView.findViewById(R.id.text_view_photo_text);
        }
    }

    public void release() {
        context = null;
    }
}
