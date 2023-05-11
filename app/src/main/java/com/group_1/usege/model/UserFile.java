package com.group_1.usege.model;

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
public class UserFile {
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
}
