package com.group_1.usege.album.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.layout.adapter.AlbumAdapter;
import com.group_1.usege.library.service.MasterAlbumService;
import com.group_1.usege.library.service.MasterAlbumServiceGenerator;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.library.service.MasterFileServiceGenerator;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.UserAlbum;
import com.group_1.usege.model.UserFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

@AndroidEntryPoint
public class AlbumListFragment extends Fragment {
    TextView totalImage;

    public RecyclerView rcvPhoto;

    public AlbumAdapter albumAdapter;
    private List<UserAlbum> lstAlbum = new ArrayList<UserAlbum>() {
        {
            add(new UserAlbum("", "favorite", "", 0, null)); // favorite album
            add(new UserAlbum("", "trash", "", 0, null)); // trash album
        }
    };
    private Context context = null;

    @Inject
    public MasterAlbumServiceGenerator masterAlbumServiceGenerator;
    @Inject
    public TokenRepository tokenRepository;
    public int LIMIT = 999;

    public AlbumListFragment() {
        // Required empty public constructor
    }

    public static AlbumListFragment newInstance(List<Album> albums) {
        AlbumListFragment fragment = new AlbumListFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_albums", (Serializable) albums);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            lstAlbum = (List<Album>) getArguments().getSerializable("List_albums");
        }

        try {
            context = getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        albumAdapter = new AlbumAdapter(lstAlbum, context, "list");

        Single<MasterAlbumService.QueryResponse<UserAlbum>> results = paging();
        results
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe(this::handleAfterCall);
    }

    private void handleAfterCall(MasterAlbumService.QueryResponse<UserAlbum> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Get Album error!");
        else {
            List<UserAlbum> albums = response.getResponse();
            System.out.println("Album size " + albums.size());
            lstAlbum.addAll(albums);
            albumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layoutImageList = (LinearLayout) inflater.inflate(R.layout.fragment_album_list, null);

        rcvPhoto = layoutImageList.findViewById(R.id.rcv_photo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvPhoto.setLayoutManager(linearLayoutManager);
        rcvPhoto.setAdapter(albumAdapter);

        return layoutImageList;
    }

    private Single<MasterAlbumService.QueryResponse<UserAlbum>> paging() {
        return masterAlbumServiceGenerator
                .getService()
                .getAlbums(tokenRepository.getToken().getUserId(), LIMIT);
    }
}