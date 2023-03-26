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
import com.group_1.usege.modle.Image;

import java.util.List;

public class RecycleListAdapter extends RecyclerView.Adapter<RecycleListAdapter.ViewHolder> {
    private List<Image> lstImage;
    private Context context;
    private String displayView = "";

    public RecycleListAdapter(List<Image> lstImage, Context context, String displayView) {
        this.lstImage = lstImage;
        this.context = context;
        this.displayView = displayView;
    }
    @NonNull
    @Override
    public RecycleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = lstImage.get(position);
        Uri uri = image.getUri();
        Glide.with(context)
                .load(uri)
                .into(holder.imgView);

        if (displayView.equals("list")) {
            holder.description.setText(image.getDescription());
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
        ImageView imgView;
        TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_thumbnail);
            description = itemView.findViewById(R.id.text_view_description);
        }
    }
}
