package com.group_1.usege.layout.adapter;

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

import java.util.List;

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
            view = inflater.inflate(R.layout.layout_item_list, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
//        Uri uri = uriList.get(position);
//        if(uri == null) {
//            return;
//        }
//
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//            if (bitmap != null) {
//                holder.imgView.setImageBitmap(bitmap);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        //holder.imgView.setImageURI(uriList.get(position));

//        Glide.with(context)
//                .load(uriList.get(position))
//                .into(holder.imgView);

        // Load ảnh ra layout
        Album image = lstAlbum.get(position);

//        Uri uri = image.getUri();
//        Glide.with(context)
//                .load(uri)
//                .into(holder.imgView);

        if (displayView.equals("card")) {
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
        ImageView imgView;
        TextView albumTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (displayView.equals("card")) {
                albumTextView = itemView.findViewById(R.id.album_name_label);
            }
            else {
                imgView = itemView.findViewById(R.id.image_view_thumbnail);
                albumTextView = itemView.findViewById(R.id.album_name_label);
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
