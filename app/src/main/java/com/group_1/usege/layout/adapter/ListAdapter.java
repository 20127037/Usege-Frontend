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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Image> lstImage;
    private Context context;
    private IClickItemImageListener iClickItemImageListener;
    private String albumMode = Album.album_mode_default;

    public ListAdapter(List<Image> lstImage, Context context, IClickItemImageListener listener, String albumMode) {
        this.lstImage = lstImage;
        this.context = context;
        this.iClickItemImageListener = listener;
        this.albumMode = albumMode;
    }

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

        switch (albumMode) {
            case Album.album_mode_default:
//                    holder.description.setText("... days");
//                holder.imgViewMore.setVisibility(View.VISIBLE);
                break;
            case Album.album_mode_trash:
                holder.description.setText("... days");
//                holder.imgViewMore.setVisibility(View.GONE);
                break;
            case Album.album_mode_favorite:
//                    holder.description.setText("... days");
//                holder.imgViewMore.setVisibility(View.VISIBLE);
                break;
            default:
//                holder.imgViewMore.setVisibility(View.VISIBLE);
        }
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
        ImageView imgView, imgViewMore;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_photo);
            description = itemView.findViewById(R.id.text_view_description);
            layoutItemList = itemView.findViewById(R.id.layout_item_list);
//            imgViewMore = itemView.findViewById(R.id.image_view_more);
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

