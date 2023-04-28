package com.group_1.usege.library.paging;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.PagingRx;
import androidx.paging.rxjava3.RxPagingSource;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.library.model.PexelsPageResponse;
import com.group_1.usege.model.Image;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.CoroutineScope;

class ImageComparator extends DiffUtil.ItemCallback<Image> {
    @Override
    public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
        return oldItem.equals(newItem);
    }
}

class ImageListAdapter extends PagingDataAdapter<Image, ImageListAdapter.ImageViewHolder> {
    ImageListAdapter(@NotNull DiffUtil.ItemCallback<Image> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ImageListAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image item = getItem(position);
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        CardView layoutItemCard;
        ImageView imgView;
        TextView photoText;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.image_view_photo);
            layoutItemCard = itemView.findViewById(R.id.layout_item_card);
            photoText = itemView.findViewById(R.id.text_view_photo_text);
        }

        public void bind(Image img)
        {

        }
    }

}


public class ImagePagingResponse<S>
{
    private ArrayList<Image> images;
    private Integer page;
    private Integer perPage;
    private Integer totalResults;
    private S nextPage;
    private S prevPage;

    public ArrayList<Image> getImages() {
        return images;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public S getNextPage() {
        return nextPage;
    }

    public S getPrevPage() {
        return prevPage;
    }
}


public interface ImageProvider<S>
{
    Single<ImagePagingResponse<S>> paging(S key);
}

public class ImagePagingSource<S> extends RxPagingSource<S, Image> {
    @NonNull
    private ImageProvider<S> provider;

    ImagePagingSource(@NonNull ImageProvider<S> provider) {
        this.provider = provider;
    }

    @NotNull
    @Override
    public Single<LoadResult<S, Image>> loadSingle(
            @NotNull LoadParams<S> params) {
        // Start refresh at page 1 if undefined.
        S nextPageNumber = params.getKey();
        return provider.paging(nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(this::toLoadResult)
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<S, Image> toLoadResult(@NonNull ImagePagingResponse<S> response) {
        return new LoadResult.Page<>(
                response.getImages(),
                null, // Only paging forward.
                response.getNextPage());
    }

    @Override
    public S getRefreshKey(@NotNull PagingState<S, Image> pagingState) {
        return null;
    }
}


