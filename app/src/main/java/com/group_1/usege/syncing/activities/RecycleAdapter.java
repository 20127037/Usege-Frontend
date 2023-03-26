package com.group_1.usege.syncing.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;

import java.io.IOException;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private List<Uri> uriList;
    private Context context;

    public RecycleAdapter(List<Uri> uriList, Context context ) {
        this.uriList = uriList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ViewHolder holder, int position) {
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

        Glide.with(context)
                .load(uriList.get(position))
                .into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        if (uriList != null) {
            return uriList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_photo);
        }
    }
}
