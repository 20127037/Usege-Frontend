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
public class UserAlbum {
    private String userId;
    private String name;
    private String createdDate;
    private int imgCount;
}
