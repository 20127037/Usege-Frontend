package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.library.adapter.ImagesAdapter;
import com.group_1.usege.library.adapter.SimpleImagesAdapter;
import com.group_1.usege.library.paging.PagingProvider;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.library.service.MasterFileServiceGenerator;
import com.group_1.usege.library.utilities.comparators.ImageComparator;
import com.group_1.usege.library.viewModel.UsegeImageViewModel;
import com.group_1.usege.model.Image;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.utilities.adapter.LoadStateAdapter;
import com.group_1.usege.utilities.interfaces.ClickItemReceiver;
import com.group_1.usege.utilities.interfaces.LongClickItemReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalByItemReceiver;
import com.group_1.usege.utilities.modules.ActivityModule;

import java.util.Map;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Single;


@AndroidEntryPoint
public class ImageListFragment extends ImageCollectionFragment<ImageListFragment.ViewHolder> {

    public ImageListFragment() {
        // Required empty public constructor
    }

    public static ImageListFragment newInstance() {
        return new ImageListFragment();
    }

    @Override
    public ImagesAdapter<ViewHolder> provideImageAdapter() {
        return new Adapter(comparator, requestManager, clickItemReceiver, longClickItemReceiver);
    }

    @Override
    public RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public int provideLayout() {
        return R.layout.fragment_image_list;
    }

    public static class Adapter extends ImagesAdapter<ImageListFragment.ViewHolder>
    {

        public Adapter(@NonNull DiffUtil.ItemCallback<Image> diffCallback,
                                   RequestManager glide,
                                   ClickItemReceiver<Image, ImagesAdapter.ImageViewHolder> clickItemReceiver,
                       LongClickItemReceiver<Image, ImagesAdapter.ImageViewHolder> longClickItemReceiver) {
            super(diffCallback, glide, clickItemReceiver, longClickItemReceiver);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_item_list, parent, false);
            return new ViewHolder(view);
        }
    }

    public static class ViewHolder extends ImagesAdapter.ImageViewHolder
    {
        TextView description;
        public ViewHolder(@NonNull View view) {
            super(view);
            description = view.findViewById(R.id.text_view_description);
            layoutContainer = view.findViewById(R.id.layout_item_list);
        }

        @Override
        public void bind(Image img, RequestManager glide,
                         ClickItemReceiver<Image, ImagesAdapter.ImageViewHolder> viewDetailsSignalReceiver,
                         LongClickItemReceiver<Image, ImagesAdapter.ImageViewHolder> longClickItemReceiver,
                         int pos)
        {
            super.bind(img, glide, viewDetailsSignalReceiver, longClickItemReceiver, pos);
            description.setText(setUpDescription(img.getDescription()));
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
    }
}