package com.group_1.usege.utilities.mappers;

import android.net.Uri;

import com.group_1.usege.model.Image;
import com.group_1.usege.model.UserFile;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserFileToImage implements Mapper<UserFile, Image> {
    @Inject
    public UserFileToImage() {
    }

    @Override
    public Image map(UserFile value) {
        int remain = 0;
        if (value.getRemainingDays() != null) {
            remain = value.getRemainingDays();
        }
        return Image.builder()
            .id(value.getFileName())
            .tags(value.getTags())
            .description(value.getDescription())
            .date(value.getDate())
            .size(value.getSizeInKb())
            .location(value.getLocation())
            .uri(Uri.parse(Uri.decode(value.getNormalUri())))
            .smallUri(Uri.parse(Uri.decode(value.getTinyUri())))
                .remainedDay(remain)
            .build();
    }
}
