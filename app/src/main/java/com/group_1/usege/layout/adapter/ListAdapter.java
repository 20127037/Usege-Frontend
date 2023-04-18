package com.group_1.usege.layout.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private Context context;
    private List<Image> lstImage;
    private IClickItemImageListener iClickItemImageListener;
    private String albumMode = Album.album_mode_default;

    public ListAdapter(List<Image> lstImage, Context context, IClickItemImageListener listener, String albumMode) {
        this.lstImage = lstImage;
        this.context = context;
        this.iClickItemImageListener = listener;
        this.albumMode = albumMode;
    }
    private Boolean isLoadingAdd = false;

    public ListAdapter(Context context, IClickItemImageListener listener) {
        //this.lstImage = lstImage;
        this.context = context;
        this.iClickItemImageListener = listener;
    }

    public void setData(List<Image> images) {
        this.lstImage = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (lstImage != null && position == lstImage.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }

        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.layout_item_list, parent, false);
            return new ImageViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int finalPosition = position;

        if (holder.getItemViewType() == TYPE_ITEM) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            // Load ảnh ra layout
            Image image = lstImage.get(finalPosition);

            Uri uri = image.getUri();
            Glide.with(context)
                    .load(uri)
                    .into(imageViewHolder.imgView);

            String newDescription = setUpDescription(image.getDescription());
            imageViewHolder.description.setText(newDescription);


            imageViewHolder.layoutItemList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItemImageListener.onClickItemImage(image, finalPosition);
                }
            });

            imageViewHolder.layoutItemList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
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

    }

    @Override
    public int getItemCount() {
        if (lstImage != null) {
            return lstImage.size();
        }

        return 0;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutItemList;
        ImageView imgView, imgViewMore;
        TextView description;
        public ImageViewHolder(@NonNull View itemView) {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_photo);
            description = itemView.findViewById(R.id.text_view_description);
            layoutItemList = itemView.findViewById(R.id.layout_item_list);
//            imgViewMore = itemView.findViewById(R.id.image_view_more);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
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

    public void addFooterLoading() {
        isLoadingAdd = true;
        lstImage.add(new Image());
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int position = lstImage.size() - 1;
        Image image = lstImage.get(position);
        if (image != null) {
            lstImage.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void release() {
        context = null;
    }
}

