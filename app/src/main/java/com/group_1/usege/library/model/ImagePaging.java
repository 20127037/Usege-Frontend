package com.group_1.usege.library.model;

import com.group_1.usege.model.Image;

import java.util.ArrayList;

public class ImagePaging<S> {
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
