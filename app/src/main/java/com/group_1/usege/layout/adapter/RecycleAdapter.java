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
import com.group_1.usege.GlideAppModule;
import com.group_1.usege.R;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.modle.Image;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private List<Image> lstImage;
    private Context context;
    private String displayView = "";
    private IClickItemImageListener iClickItemImageListener;

    public RecycleAdapter(List<Image> lstImage, Context context, String displayView,  IClickItemImageListener listener) {
        this.lstImage = lstImage;
        this.context = context;
        this.iClickItemImageListener = listener;
        this.displayView = displayView;
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (displayView.equals("card")) {
            view = inflater.inflate(R.layout.item_photo, parent, false);
        }
        else if (displayView.equals("list")) {
            view = inflater.inflate(R.layout.layout_item_list, parent, false);
        }

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

//        Glide.with(context)
//                .load(uriList.get(position))
//                .into(holder.imgView);

        // Load ảnh ra layout
        Image image = lstImage.get(position);

        Uri uri = image.getUri();
        Glide.with(context)
                .load(uri)
                .into(holder.imgView);

        if (displayView.equals("list")) {
            String newDescription = setUpDescription(image.getDescription());
            holder.description.setText(newDescription);
        }

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemImageListener.onClickItemImage(image, position);
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
        LinearLayout layoutItem;
        ImageView imgView;
        TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (displayView.equals("card")) {
                imgView = itemView.findViewById(R.id.image_view_photo);
                layoutItem = itemView.findViewById(R.id.layout_item_card);
            }
            else {
                imgView = itemView.findViewById(R.id.image_view_thumbnail);
                description = itemView.findViewById(R.id.text_view_description);
                layoutItem = itemView.findViewById(R.id.layout_item_list);
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

    public void release() {
        context = null;
    }
}
