package com.group_1.usege.modle;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Album implements Serializable {
    private String name;

    private List<Image> albumImages;

    public Album(String name) {
        this.name = name;
        this.albumImages = new ArrayList<Image>();
    }

    public Album(String name, List<Image> albumImages) {
        this.name = name;
        this.albumImages = albumImages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(List<Image> albumImages) {
        this.albumImages = albumImages;
    }
}
