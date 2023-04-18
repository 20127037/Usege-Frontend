package com.group_1.usege.pagination;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager linearLayoutManager = null;
    private GridLayoutManager gridLayoutManager = null;

    public PaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public PaginationScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = 0;
        int totalItemCount = 0;
        int firstVisibleItemPosition = 0;

        if (gridLayoutManager == null) {
            visibleItemCount = linearLayoutManager.getChildCount();
            totalItemCount = linearLayoutManager.getItemCount();
            firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        } else if (linearLayoutManager == null){
            visibleItemCount = gridLayoutManager.getChildCount();
            totalItemCount = gridLayoutManager.getItemCount();
            firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
        }

//        Log.e("visibleItemCount", "visibleItemCount " + visibleItemCount);
//        Log.e("totalItemCount", "totalItemCount " + totalItemCount);
//        Log.e("firstVisibleItemPosition", "firstVisibleItemPosition " + firstVisibleItemPosition);

        if (isLoading() || isLastPage()) {
            return;
        }

        if (firstVisibleItemPosition >= 0 && ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount)) {
            loadMoreItems();
        }
    }

    public abstract void loadMoreItems();
    public abstract Boolean isLoading();
    public abstract Boolean isLastPage();
}
