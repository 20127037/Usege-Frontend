package com.group_1.usege.layout.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.modle.Album;
import com.group_1.usege.syncing.activities.LibraryActivity;

import java.util.List;
import java.util.Objects;

public class AlbumRadioAdapter extends RecyclerView.Adapter<AlbumRadioAdapter.ViewHolder> {

    private List<Album> lstAlbum;
    private Context context;
    private static RadioButton lastChecked = null;

    private static int lastCheckPosition = -1;

    public AlbumRadioAdapter(List<Album> lstAlbum, Context context) {
        this.lstAlbum = lstAlbum;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumRadioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_choose_destination_album_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumRadioAdapter.ViewHolder holder, int position) {

        // set name for album
        holder.albumRadio.setText(lstAlbum.get(position).getName());

        holder.albumRadio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RadioButton cb = (RadioButton)v;
                if(cb.isChecked())
                {
                    if(lastChecked != null)
                    {
                        lastChecked.setChecked(false);
                    }

                    lastChecked = cb;
                    lastCheckPosition = position;
                }
                else
                    lastChecked = null;

                if (context.getClass().equals(LibraryActivity.class)) {
                    System.out.println(1);
//                        ((LibraryActivity)context).showCreateAlbumBottomSheet();
                    Activity activity = (Activity) context;
                    if (activity instanceof LibraryActivity) {
                        System.out.println(2);
                        LibraryActivity libActivity = (LibraryActivity) activity;
                        libActivity.setDestinationAlbum(lstAlbum.get(position));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (lstAlbum != null) {
            return lstAlbum.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton albumRadio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumRadio = itemView.findViewById(R.id.radio_album_item);
        }
    }
}
