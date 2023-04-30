package com.group_1.usege.library.model;


import com.group_1.usege.model.Image;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagePaging<S> {
    private List<Image> images;
    private S page;
    private Integer perPage;
    private Integer totalResults;
    private S nextPage;
    private S prevPage;
}
