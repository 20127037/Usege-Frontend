package com.group_1.usege.layout.adapter;

import android.content.Context;
import android.net.Uri;
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

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Image> lstImage;
    private Context context;
    private IClickItemImageListener iClickItemImageListener;

    public ListAdapter(List<Image> lstImage, Context context, IClickItemImageListener listener) {
        this.lstImage = lstImage;
        this.context = context;
        this.iClickItemImageListener = listener;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        view = inflater.inflate(R.layout.layout_item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        final int finalPosition = position;
        // Load ảnh ra layout
        Image image = lstImage.get(finalPosition);

        Uri uri = image.getUri();
        Glide.with(context)
                .load(uri)
                .into(holder.imgView);

        String newDescription = setUpDescription(image.getDescription());
        holder.description.setText(newDescription);


        holder.layoutItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemImageListener.onClickItemImage(image, finalPosition);
            }
        });

        holder.layoutItemList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
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
        LinearLayout layoutItemList;
        ImageView imgView;
        TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_photo);
            description = itemView.findViewById(R.id.text_view_description);
            layoutItemList = itemView.findViewById(R.id.layout_item_list);
        }
    }

    public String setUpDescription(String curDescription) {
        String newDescription = "";

        if (curDescription != null) {
            newDescription = curDescription;

            if (curDescription.length() > 16) {
                newDescription = curDescription.substring(0, 16);
                newDescription += "...";
            }
        }

        return newDescription;
    }

    public void release() {
        context = null;
    }
}

