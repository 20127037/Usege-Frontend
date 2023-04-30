package com.group_1.usege.library.model;

import android.net.Uri;
import android.util.Log;

import com.group_1.usege.model.Image;
import com.group_1.usege.utilities.mappers.Mapper;

import java.util.stream.Collectors;

public class PexelsPageResponseMapper implements Mapper<PexelsPageResponse, ImagePaging<Integer>> {
    @Override
    public ImagePaging<Integer> map(PexelsPageResponse value) {
        Log.i("Mapper", "Pexels map");
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
                        .uri(Uri.parse(i.getSrc().getOriginal()))
                        .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
