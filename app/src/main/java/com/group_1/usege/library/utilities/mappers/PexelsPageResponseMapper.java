package com.group_1.usege.library.utilities.mappers;

import android.net.Uri;
import android.util.Log;

import com.group_1.usege.library.model.ImagePaging;
import com.group_1.usege.library.model.PexelsPageResponse;
import com.group_1.usege.model.Image;
import com.group_1.usege.utilities.mappers.Mapper;

import java.util.stream.Collectors;

public class PexelsPageResponseMapper implements Mapper<PexelsPageResponse, ImagePaging<Integer>> {
    @Override
    public ImagePaging<Integer> map(PexelsPageResponse value) {
        Integer nextPage = value.getNextPage() != null ? value.getPage() + 1 : null;
        Integer prevPage = value.getPrevPage() != null ? value.getPage() - 1 : null;
        return ImagePaging.<Integer>builder()
                .page(value.getPage())
                .perPage(value.getPerPage())
                .totalResults(value.getTotalResults())
                .nextPage(nextPage)
                .prevPage(prevPage)
                .images(value.getImages().stream().map(i -> Image.builder()
                                .id(String.valueOf(i.getId()))
                                .description(i.getPhotographer())
                                .uri(Uri.parse(i.getSrc().getOriginal()))
                                .smallUri(Uri.parse(i.getSrc().getTiny()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
