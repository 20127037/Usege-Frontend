package com.group_1.usege.library.utilities.mappers;

import android.net.Uri;

import com.group_1.usege.library.model.ImagePaging;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.model.Image;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.utilities.mappers.Mapper;

import java.util.Map;
import java.util.stream.Collectors;

public class UsegeFilePageResponseMapper implements Mapper<MasterFileService.QueryResponse<UserFile>, ImagePaging<Map<String, String>>> {

    @Override
    public ImagePaging<Map<String, String>> map(MasterFileService.QueryResponse<UserFile> value) {
        return ImagePaging.<Map<String, String>>builder()
                .page(null)
                .perPage(null)
                .totalResults(null)
                .nextPage(value.getNextEvaluatedKey())
                .prevPage(null)
                .images(value.getResponse().stream().map(i -> Image.builder()
                                .id(String.valueOf(i.getFileName()))
                                .description(i.getDescription())
                                .uri(Uri.parse(i.getNormalUri()))
                                .smallUri(Uri.parse(i.getTinyUri()))
                                .date(i.getUpdated())
                                .tags(i.getTags())
                                .location(i.getLocation())
                                .size(i.getSizeInKb())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
