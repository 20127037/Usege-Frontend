package com.group_1.usege.library.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@FieldNameConstants
@AllArgsConstructor
public class UserFile {
    private String userId;
    private String fileName;
    private String contentType;
    private Long sizeInKb;
    private String updated;
    private String fileUrl;
    private List<String> tags;
    private String description;
    private String date;
    private String location;
    private String originalUri;
    private Boolean isFavourite;
    private Boolean isDeleted;
    private Integer remainingDays;
}
