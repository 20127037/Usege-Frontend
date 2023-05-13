package com.group_1.usege.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFile implements Parcelable {
    private String userId;
    private String fileName;
    private String contentType;
    private Long sizeInKb;
    private String updated;
    private List<String> tags;
    private String description;
    private String date;
    private String location;
    //uri to normal (bigger) version of image
    private String normalUri;
    //uri to tiny version of image
    private String tinyUri;
    private Boolean isFavourite;
    private Integer remainingDays;
    private Set<String> previousAlbums;

    protected UserFile(Parcel in) {
        userId = in.readString();
        fileName = in.readString();
        contentType = in.readString();
        if (in.readByte() == 0) {
            sizeInKb = null;
        } else {
            sizeInKb = in.readLong();
        }
        updated = in.readString();
        tags = in.createStringArrayList();
        description = in.readString();
        date = in.readString();
        location = in.readString();
        normalUri = in.readString();
        tinyUri = in.readString();
        byte tmpIsFavourite = in.readByte();
        isFavourite = tmpIsFavourite == 0 ? null : tmpIsFavourite == 1;
        if (in.readByte() == 0) {
            remainingDays = null;
        } else {
            remainingDays = in.readInt();
        }
    }

    public static final Creator<UserFile> CREATOR = new Creator<UserFile>() {
        @Override
        public UserFile createFromParcel(Parcel in) {
            return new UserFile(in);
        }

        @Override
        public UserFile[] newArray(int size) {
            return new UserFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(fileName);
        parcel.writeString(contentType);
        if (sizeInKb == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(sizeInKb);
        }
        parcel.writeString(updated);
        parcel.writeStringList(tags);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(location);
        parcel.writeString(normalUri);
        parcel.writeString(tinyUri);
        parcel.writeByte((byte) (isFavourite == null ? 0 : isFavourite ? 1 : 2));
        if (remainingDays == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(remainingDays);
        }
    }
}
