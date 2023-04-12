package com.group_1.usege.layout.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.modle.Album;
import com.group_1.usege.syncing.activities.LibraryActivity;

import java.util.List;
import java.util.Objects;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> lstAlbum;
    private Context context;
    private String displayView = "";

    public AlbumAdapter(List<Album> lstImage, Context context, String displayView) {
        this.lstAlbum = lstImage;
        this.context = context;
        this.displayView = displayView;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (displayView.equals("card")) {
            view = inflater.inflate(R.layout.item_photo_album, parent, false);
        }
        else if (displayView.equals("list")) {
            view = inflater.inflate(R.layout.layout_album_item_list, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {

        // Load ảnh ra layout
        Album image = lstAlbum.get(position);

        if (displayView.equals("card")) {
            // set name for album
            String albumName = image.getName();
            holder.albumTextView.setText(albumName);

            // set image for album if not default (favorite or trash)
            if(Objects.equals(albumName, "favorite")) {
                holder.albumImgView.setImageResource(R.drawable.album_favorite_img);
            } else if (Objects.equals(albumName, "trash")) {
                holder.albumImgView.setImageResource(R.drawable.album_trash_img);
            }

            holder.albumImgView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (context.getClass().equals(LibraryActivity.class)) {
                        Activity activity = (Activity) context;
                        if (activity instanceof LibraryActivity) {
                            LibraryActivity libActivity = (LibraryActivity) activity;
                            libActivity.clickOpenAlbumImageList(lstAlbum.get(position));
                        }
                    }
                }
            });
        }

        if (displayView.equals("list")) {
            // set name for album
            String albumName = image.getName();
            holder.albumTextView.setText(albumName);
        }
    }

    @Override
    public int getItemCount() {
        if (lstAlbum != null) {
            return lstAlbum.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImgView;
        TextView albumTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (displayView.equals("card")) {
                albumImgView = itemView.findViewById(R.id.image_album_view_photo);
                albumTextView = itemView.findViewById(R.id.album_name_label);
            }
            else {
//                albumImgView = itemView.findViewById(R.id.image_album_view_photo);
                albumTextView = itemView.findViewById(R.id.text_view_album_name);
            }
        }
    }

    public String setUpDescription(String curDescription) {
        String newDescription = curDescription;

        if (curDescription.length() > 16) {
            newDescription = curDescription.substring(0, 16);
            newDescription += "...";
        }

        return newDescription;
    }
}
