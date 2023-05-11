package com.group_1.usege.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Parcelable {
    private List<String> tags;
    private String description;
    private String date;
    private long size;
    private String location;
    private Uri uri;
    private String id;
    private Uri smallUri;

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

    public Image(Image img) {
        this.uri = img.getUri();
        this.id = img.getId();
        this.date = img.getDate();
        this.description = img.getDescription();
        this.size = img.getSize();
        this.location = img.location;
        this.tags = img.tags;
    }

    protected Image(Parcel in) {
        id = in.readString();
        date = in.readString();
        size = in.readLong();
        description = in.readString();
        location = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        tags = new ArrayList<>();
        in.readList(tags, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(date);
        dest.writeLong(size);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeParcelable(uri, flags);
        dest.writeList(tags);
    }
}
