package com.group_1.usege.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Image implements Parcelable {
    private List<String> tags;
    private String description;
    private String date;
    private long size;
    private String location;
    private Uri uri;


    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<String> getTags() { return tags; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image() {
        // Nothing
    }

    public Image(Uri uri, List<String> tags, String description, String date, long size, String location) {
        this.uri = uri;
        this.tags = tags;
        this.description = description;

        if (date == null) this.date = "";
        else {
            String day = date.split(" ")[0];
            List<String> dayComponents = Arrays.asList(day.split(":"));
            String reversedDay = dayComponents.get(2).concat("/").concat(dayComponents.get(1)).concat("/").concat(dayComponents.get(0));
            this.date = reversedDay;
        }

        this.size = size;

        // Thực hiện chuyển đổi ở đây trước khi gán giá trị
        this.location = location;
        //////////////////
    }

    protected Image(Parcel in) {
        date = in.readString();
        size = in.readLong();
        description = in.readString();
        location = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeLong(size);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeParcelable(uri, flags);
    }
}
