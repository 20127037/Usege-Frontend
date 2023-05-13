package com.group_1.usege.album.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.layout.adapter.AlbumAdapter;
import com.group_1.usege.library.service.MasterAlbumService;
import com.group_1.usege.library.service.MasterAlbumServiceGenerator;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.UserAlbum;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Single;

@AndroidEntryPoint
public class AlbumCardFragment extends Fragment {
    FragmentTransaction ft;
    TextView totalImage;

    public RecyclerView rcvPhoto;

    public AlbumAdapter albumAdapter;
    private List<Album> lstImage;
    private Context context = null;
    public AlbumCardFragment() {
        // Required empty public constructor
    }
    @Inject
    public MasterAlbumServiceGenerator masterAlbumServiceGenerator;
    @Inject
    public TokenRepository tokenRepository;

    public static AlbumCardFragment newInstance(List<Album> images) {
        AlbumCardFragment fragment = new AlbumCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_album_images", (Serializable) images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            lstImage = (List<Album>) getArguments().getSerializable("List_album_images");
        }

        try {
            context = getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        Single<MasterAlbumService.QueryResponse<UserAlbum>> results = paging();
        System.out.println(results);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutImageCard = inflater.inflate(R.layout.fragment_image_card, null);

        rcvPhoto = layoutImageCard.findViewById(R.id.rcv_photo);

        albumAdapter = new AlbumAdapter(lstImage, context, "card");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(albumAdapter);

        return layoutImageCard;
    }

    private Single<MasterAlbumService.QueryResponse<UserAlbum>> paging() {
        return masterAlbumServiceGenerator
                .getService()
                .getAlbums(tokenRepository.getToken().getUserId());
    }
}
