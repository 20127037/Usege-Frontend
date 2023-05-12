package com.group_1.usege.album.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.Image;
import com.group_1.usege.layout.adapter.CardAdapter;
import com.group_1.usege.layout.adapter.ListAdapter;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.pagination.PaginationScrollListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlbumImageListFragment extends Fragment {
    LibraryActivity libraryActivity;
    public int position;

    public RecyclerView rcvPhoto;
    public CardAdapter cardAdapter;
    public ListAdapter listAdapter;
    private List<Image> lstVisibleImage;
    private Album album;
    private String albumMode = Album.album_mode_default;
    private String mode;
    private Context context = null;
    private Boolean isLoading = false;
    private Boolean isLastPage = false;
    private int totalPage;
    private int currentPage = 1;

    private static final int countItemInPage = 5;

    public AlbumImageListFragment() {
        // Required empty public constructor
    }

    public static AlbumImageListFragment newInstance(Album album, String mode) {
        AlbumImageListFragment fragment = new AlbumImageListFragment();
        Bundle args = new Bundle();
        args.putParcelable("album", (Parcelable) album);
        args.putSerializable("album_mode", (Serializable) mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            album = (Album) getArguments().getParcelable("album");
            if(Objects.equals(album.getName(), "trash")) {
                albumMode = Album.album_mode_trash;
            }
            mode = (String) getArguments().getSerializable("album_mode");
            totalPage = album.getAlbumImages().size() / countItemInPage + 1;
        }

        try {
            context = getActivity();
            libraryActivity = (LibraryActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layoutImageList = (LinearLayout) inflater.inflate(R.layout.fragment_image_list_album, null);


        rcvPhoto = layoutImageList.findViewById(R.id.rcv_photo);
        LinearLayout layoutListTitle = layoutImageList.findViewById(R.id.layout_list_title);
        TextView albumName = layoutImageList.findViewById(R.id.text_view_album_name);
        TextView albumSubtitle = layoutImageList.findViewById(R.id.text_view_album_sub_title);
        TextView headerRight = layoutImageList.findViewById(R.id.layout_header_right);
        ImageView backImageView = layoutImageList.findViewById(R.id.image_view_backward);
        ImageView imageViewMore = layoutImageList.findViewById(R.id.image_view_more);

        imageViewMore.setOnClickListener(v -> {
            if(albumMode == Album.album_mode_trash) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.image_selection_album_trash_more_options, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    Activity activity = (Activity) context;
                    switch (item.getItemId()) {
                        case R.id.restore_all_menu_item:
                            if (activity instanceof LibraryActivity) {
                                LibraryActivity libActivity = (LibraryActivity) activity;
                                libActivity.restoreAllTrash();
                            }
                            // Do something when the "Favorite" item is clicked
                            return true;
                        case R.id.empty_bin_menu_item:
                            // Do something when the "Combine" item is clicked
                            if (activity instanceof LibraryActivity) {
                                LibraryActivity libActivity = (LibraryActivity) activity;
                                libActivity.clearTrash();
                            }
                            return true;
                        default:
                            return false;
                    }
                });
                popupMenu.show();
                return;
            }
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenuInflater().inflate(R.menu.image_selection_album_more_options, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                Activity activity = (Activity) context;
                switch (item.getItemId()) {
                    case R.id.rename_image_menu_item:
                        // Do something when the "rename" item is clicked
                        if (activity instanceof LibraryActivity) {
                            LibraryActivity libActivity = (LibraryActivity) activity;
                            libActivity.renameAlbum(album);
                        }
                        return true;
                    case R.id.make_a_presentation_menu_item:
                        // Do something when the "Combine" item is clicked
                        return true;
                    case R.id.unlock_image_menu_item:
                        // Do something when the "Compress" item is clicked
                        return true;
                    case R.id.compress_menu_item:
                        // Do something when the "Make a presentation" item is clicked
                        return true;
                    case R.id.delete_menu_item:
                        // Do something when the "Delete" item is clicked
                        if (activity instanceof LibraryActivity) {
                            LibraryActivity libActivity = (LibraryActivity) activity;
                            libActivity.deleteAlbum(album);
                        }
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });

        if(albumMode == Album.album_mode_trash) {
            headerRight.setText("Left time");
        }

        albumName.setText(album.getName());
        albumSubtitle.setText(String.format("%d images", album.getAlbumImages().size()));
        backImageView.setOnClickListener(v -> {
            if (context.getClass().equals(LibraryActivity.class)) {
                Activity activity = (Activity) context;
                if (activity instanceof LibraryActivity) {
                    LibraryActivity libActivity = (LibraryActivity) activity;
                    libActivity.triggerAlbumButton();
                    libActivity.setShowLayoutLibFuntions();
                }
            }
        });

        //recycleAdapter = new RecycleAdapter(album.getAlbumImages(), context, mode);
//        recycleAdapter = new RecycleAdapter(album.getAlbumImages(), context, mode, albumMode);

        if(mode == "list") {
            listAdapter = new ListAdapter(context, new IClickItemImageListener() {
                @Override
                public void onClickItemImage(Image image, int position) {
                    onClickGoToDetails(image, position);
                }
            }, albumMode);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            rcvPhoto.setLayoutManager(linearLayoutManager);
            rcvPhoto.setAdapter(listAdapter);

            setFirstDataListAdapter();
            rcvPhoto.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                @Override
                public void loadMoreItems() {
                    isLoading = true;

                    currentPage += 1;
                    loadNextPageListAdapter();
                }

                @Override
                public Boolean isLoading() {
                    return isLoading;
                }

                @Override
                public Boolean isLastPage() {
                    return isLastPage;
                }
            });
        } else if (mode == "card") {
            layoutListTitle.setVisibility(View.GONE);
            cardAdapter = new CardAdapter(context, new IClickItemImageListener() {
                @Override
                public void onClickItemImage(Image image, int position) {
                    onClickGoToDetails(image, position);
                }
            }, albumMode);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            rcvPhoto.setLayoutManager(gridLayoutManager);
            rcvPhoto.setAdapter(cardAdapter);

            setFirstDataCardAdapter();
            rcvPhoto.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
                @Override
                public void loadMoreItems() {
                    isLoading = true;

                    currentPage += 1;
                    loadNextPageCardAdapter();
                }

                @Override
                public Boolean isLoading() {
                    return isLoading;
                }

                @Override
                public Boolean isLastPage() {
                    return isLastPage;
                }
            });

            if (context.getClass().equals(LibraryActivity.class)) {
                Activity activity = (Activity) context;
                if (activity instanceof LibraryActivity) {
                    LibraryActivity libActivity = (LibraryActivity) activity;
                    libActivity.moveToAlbum.setOnClickListener(v -> {
                        System.out.print("clicked move!");
                        libActivity.moveToAlbum(album);
                    });
                }
            }
        }

        return layoutImageList;

    }

    private void onClickGoToDetails(Image image, int position) {
        Log.e("P", "P: " + position);
        libraryActivity.sendAndReceiveImageInAlbum(image, position, album);
    }

    /**
     * Load data page 1
     */
    private void setFirstDataListAdapter() {
        position = 0;
        lstVisibleImage = getListImage();
        listAdapter.setData(lstVisibleImage);

        if (currentPage < totalPage) {
            listAdapter.addFooterLoading();
            isLastPage = false;
        } else {
            isLastPage = true;
        }
    }

    private void loadNextPageListAdapter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Image> images = getListImage();

                listAdapter.removeFooterLoading();
                lstVisibleImage.addAll(images);
                listAdapter.notifyDataSetChanged();

                isLoading = false;
                Log.e("Page", "Current " + currentPage);
                if (currentPage < totalPage) {
                    listAdapter.addFooterLoading();
                    isLastPage = false;
                } else {
                    isLastPage = true;
                }
            }
        }, 750);

    }

    private void setFirstDataCardAdapter() {
        position = 0;
        lstVisibleImage = getListImage();
        cardAdapter.setData(lstVisibleImage);

        if (currentPage < totalPage) {
            cardAdapter.addFooterLoading();
            isLastPage = false;
        } else {
            isLastPage = true;
        }
    }

    private void loadNextPageCardAdapter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Image> images = getListImage();

                cardAdapter.removeFooterLoading();
                lstVisibleImage.addAll(images);
                cardAdapter.notifyDataSetChanged();

                isLoading = false;
                Log.e("Page", "Current " + currentPage);
                if (currentPage < totalPage) {
                    cardAdapter.addFooterLoading();
                    isLastPage = false;
                } else {
                    isLastPage = true;
                }
            }
        }, 750);

    }

    private List<Image> getListImage() {
        Toast.makeText(context, "Load data page " + currentPage, Toast.LENGTH_LONG).show();

        List<Image> images = new ArrayList<>();

        for (int i = 0; i < countItemInPage; i++) {
            if (position <= album.getAlbumImages().size() - 1) {
                images.add(album.getAlbumImages().get(position));
                position += 1;
            }
        }

        return images;
    }
}