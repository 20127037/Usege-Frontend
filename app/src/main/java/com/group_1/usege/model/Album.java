package com.group_1.usege.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Parcelable {
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

    protected Album(Parcel in) {
        name = in.readString();
        int size = in.readInt();
        albumImages = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Image myObject = in.readParcelable(Image.class.getClassLoader());
            albumImages.add(myObject);
        }
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(albumImages.size());
        for (Image myObject : albumImages) {
            dest.writeParcelable(myObject, flags);
        }
    }
}
