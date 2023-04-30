package com.group_1.usege.library.model;

import com.google.gson.annotations.SerializedName;
import com.group_1.usege.utilities.mappers.Mapper;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PexelsPageResponse
{
    @SerializedName("photos")
    private ArrayList<PexelsImage> images;
    @SerializedName("page")
    private Integer page;
    @SerializedName("per_page")
    private Integer perPage;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("next_page")
    private String nextPage;
    @SerializedName("prev_page")
    private String prevPage;

    @Data
    @NoArgsConstructor
    public static class PexelsImage {
        private Long id;
        private Integer width;
        private Integer height;
        private String url;
        private String photographer;
//        private String photographerUrl;
//        private Integer photographerId;
        private Sources src;
//        private boolean liked;
//        private String alt;
    }
    @Data
    @NoArgsConstructor
    public static class Sources {
        private String original;
//        private String large2x;
//        private String large;
//        private String medium;
//        private String small;
//        private String portrait;
//        private String landscape;
//        private String tiny;
    }
}
