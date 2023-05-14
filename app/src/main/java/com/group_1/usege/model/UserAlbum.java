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
public class UserAlbum implements Parcelable {
    private String userId;
    private String name;
    private String createdDate;
    private int imgCount;
    private String password;

    protected UserAlbum(Parcel in) {
        userId = in.readString();
        name = in.readString();
        createdDate = in.readString();
        imgCount = in.readInt();
        password = in.readString();
    }

    public static final Creator<UserAlbum> CREATOR = new Creator<UserAlbum>() {
        @Override
        public UserAlbum createFromParcel(Parcel in) {
            return new UserAlbum(in);
        }

        @Override
        public UserAlbum[] newArray(int size) {
            return new UserAlbum[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(name);
        parcel.writeString(createdDate);
        parcel.writeInt(imgCount);
        parcel.writeString(password);
    }
}
