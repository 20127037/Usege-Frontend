package com.group_1.usege.layout.adapter;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.library.service.MasterAlbumService;
import com.group_1.usege.library.service.MasterAlbumServiceGenerator;
import com.group_1.usege.model.Album;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.model.UserAlbum;
import com.group_1.usege.model.UserFile;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<UserAlbum> lstAlbum;
    private Context context;
    private String displayView = "";
    public int LIMIT = 999;
    private OnClickListener mListener;
    @Inject
    public MasterAlbumServiceGenerator masterAlbumServiceGenerator;
    @Inject
    public TokenRepository tokenRepository;

    public AlbumAdapter(List<UserAlbum> lstAlbum, Context context, String displayView) {
        this.lstAlbum = lstAlbum;
        this.context = context;
        this.displayView = displayView;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        System.out.println("Refresh!!!");

        if (displayView.equals("card")) {
            view = inflater.inflate(R.layout.item_photo_album, parent, false);
        }
        else if (displayView.equals("list")) {
            view = inflater.inflate(R.layout.layout_album_item_list, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });

        //  ================== Load ảnh ra layout
        UserAlbum image = lstAlbum.get(position);

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

//            holder.albumImgView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    if (context.getClass().equals(LibraryActivity.class)) {
//                        Activity activity = (Activity) context;
//                        if (activity instanceof LibraryActivity) {
//                            LibraryActivity libActivity = (LibraryActivity) activity;
////                            libActivity.clickOpenAlbumImageList(lstAlbum.get(position));
//                            System.out.println("Album size: " + lstAlbum);
//                            System.out.println("Album name: " + lstAlbum.get(position).getName());
//                        }
//                    }
//                }
//
//            });
        }

        if (displayView.equals("list")) {
            // set name for album
            String albumName = image.getName();
            holder.albumTextView.setText(albumName);
            int visibility = image.getPassword() != null ? VISIBLE : INVISIBLE;
            holder.protectView.setVisibility(visibility);
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
        ImageView protectView;
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
                protectView = itemView.findViewById(R.id.image_view_thumbnail);
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

    public interface OnClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }
}
