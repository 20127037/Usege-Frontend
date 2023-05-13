package com.group_1.usege.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFileInAlbum {
    private String userId;
    private String updated;
    private String albumName;
    private String fileName;
}
