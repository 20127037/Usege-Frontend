package com.group_1.usege.layout.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.GlideAppModule;
import com.group_1.usege.R;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.modle.Image;
import com.group_1.usege.syncing.activities.LibraryActivity;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Image> lstImage;
    private Context context;
    private IClickItemImageListener iClickItemImageListener;

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
        holder.layoutItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemImageListener.onClickItemImage(image, finalPosition);
            }
        });

        holder.layoutItemCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LibraryActivity.openBottomMenu(image);
                v.setAlpha((float) 0.5);
                return true;
            }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_photo);
            layoutItemCard = itemView.findViewById(R.id.layout_item_card);
        }
    }

    public void release() {
        context = null;
    }
}
