package com.group_1.usege.layout.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private List<Image> lstImage;
    private Context context;
    private IClickItemImageListener iClickItemImageListener;
    private Boolean isLoadingAdd = false;

    private String albumMode = Album.album_mode_default;

    public CardAdapter(Context context, IClickItemImageListener listener, String albumMode) {
        this.context = context;
        this.iClickItemImageListener = listener;
        this.albumMode = albumMode;
    }

    public CardAdapter(Context context, IClickItemImageListener listener) {
        this.context = context;
        this.iClickItemImageListener = listener;
    }

    public CardAdapter(ArrayList<Image> firstSixItems, Context context, IClickItemImageListener listener) {
        this.context = context;
        this.iClickItemImageListener = listener;
        this.lstImage = firstSixItems;
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
            if (Objects.equals(albumMode, Album.album_mode_trash)) {
                view = inflater.inflate(R.layout.item_photo_trash_item, parent, false);
                return new CardAdapter.ImageViewHolder(view);
            } else {
                view = inflater.inflate(R.layout.item_photo, parent, false);
                return new CardAdapter.ImageViewHolder(view);
            }
        } else {
            view = inflater.inflate(R.layout.item_loading, parent, false);
            return new CardAdapter.LoadingViewHolder(view);
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
            Log.d("SIZE", "I: " + uri);

            imageViewHolder.imgView.setOnClickListener(v -> {
                if (context.getClass().equals(LibraryActivity.class)) {
                    Activity activity = (Activity) context;
                    if (activity instanceof LibraryActivity) {
                        LibraryActivity libActivity = (LibraryActivity) activity;
                        ImageView imageView = (ImageView) v;
                        if (imageView.getColorFilter() != null) {
                            // FOR UI
                            imageView.clearColorFilter();
                            // FOR LOGIC
                            libActivity.removeSingleImageAndRemoveBottomMenuIfNoImageLeft(image);
                        } else {
                            iClickItemImageListener.onClickItemImage(image, finalPosition);
                        }
                    }
                }
            });

            imageViewHolder.imgView.setOnLongClickListener(v -> {
                if (context.getClass().equals(LibraryActivity.class)) {
                    Activity activity = (Activity) context;
                    if (activity instanceof LibraryActivity) {
                        LibraryActivity libActivity = (LibraryActivity) activity;
                        ImageView imageView = (ImageView) v;
                        if (imageView.getColorFilter() == null) {
                            // FOR UI
                            imageView.setColorFilter(ContextCompat.getColor(context, R.color.chosen_image));
                            // FOR LOGIC
                            libActivity.selectSingleImageAndOpenBottomMenuIfNotYet(image);
                        }
                    }
                }
                return true;
            });

            switch (albumMode) {
                case Album.album_mode_default:
//                    imageViewHolder.photoText.setVisibility(View.GONE);
                    break;
                case Album.album_mode_trash:
                    imageViewHolder.photoText.setVisibility(View.VISIBLE);
                    imageViewHolder.photoText.setText(String.format("%d left days",image.getRemainedDay()));
                    break;
                case Album.album_mode_favorite:
//                    imageViewHolder.photoText.setVisibility(View.GONE);
                    break;
                default:
//                    imageViewHolder.photoText.setVisibility(View.GONE);
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
        CardView layoutItemCard;
        ImageView imgView;
        TextView photoText;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.image_view_photo);
            layoutItemCard = itemView.findViewById(R.id.layout);
            photoText = itemView.findViewById(R.id.text_view_photo_text);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
        }
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
