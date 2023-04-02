package com.group_1.usege.layout.adapter;

import android.annotation.SuppressLint;
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
import com.group_1.usege.syncing.activities.LibraryActivity;

import java.util.List;
import android.app.Activity;
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private List<Image> lstImage;
    private Context context;
    private String displayView = "";

    public RecycleAdapter(List<Image> lstImage, Context context, String displayView) {
        this.lstImage = lstImage;
        this.context = context;
        this.displayView = displayView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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

        if (displayView.equals("card")) {
            holder.imgView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // TODO Auto-generated method stub
                    if (context.getClass().equals(LibraryActivity.class)) {
//                        ((LibraryActivity)context).showCreateAlbumBottomSheet();
                        Activity activity = (Activity) context;
                        if (activity instanceof LibraryActivity) {
                            LibraryActivity libActivity = (LibraryActivity) activity;
                            libActivity.clickOpenAlbumCreateBottomSheetAndAddImage(position);
                        }
                    }
                    return true;
                }
            });
        }


        if (displayView.equals("list")) {
            String newDescription = setUpDescription(image.getDescription());
            holder.description.setText(newDescription);
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
        @SuppressLint({"RestrictedApi", "ResourceAsColor"})
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (displayView.equals("card")) {
                imgView = itemView.findViewById(R.id.image_view_photo);
                imgView.setOnLongClickListener(v -> {
                    LibraryActivity.openBottomMenu(imgView);
                    v.setAlpha((float) 0.5);
                    return true;
                });
            }
            else {
                imgView = itemView.findViewById(R.id.image_view_thumbnail);
                description = itemView.findViewById(R.id.text_view_description);
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
