package com.group_1.usege.library.model;

import com.group_1.usege.utilities.mappers.Mapper;

import java.util.ArrayList;

public class PexelsPageResponse
{
    private ArrayList<PexelsImage> images;
    private Integer page;
    private Integer perPage;
    private Integer totalResults;
    private String nextPage;
    private String prevPage;

    public ArrayList<PexelsImage> getImages() {
        return images;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public String getNextPage() {
        return nextPage;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public static class PexelsImage {
        private Long id;
        private Integer width;
        private Integer height;
        private String url;
        private String photographer;
        private String photographerUrl;
        private Integer photographerId;
        private Sources src;
        private boolean liked;
        private String alt;

        public Long getId() {
            return id;
        }

        public Integer getWidth() {
            return width;
        }

        public Integer getHeight() {
            return height;
        }

        public String getUrl() {
            return url;
        }

        public String getPhotographer() {
            return photographer;
        }

        public String getPhotographerUrl() {
            return photographerUrl;
        }

        public Integer getPhotographerId() {
            return photographerId;
        }

        public Sources getSrc() {
            return src;
        }

        public boolean isLiked() {
            return liked;
        }

        public String getAlt() {
            return alt;
        }
    }
    public static class Sources {
        private String original;
        private String large2x;
        private String large;
        private String medium;
        private String small;
        private String portrait;
        private String landscape;
        private String tiny;

        public String getOriginal() {
            return original;
        }

        public String getLarge2x() {
            return large2x;
        }

        public String getLarge() {
            return large;
        }

        public String getMedium() {
            return medium;
        }

        public String getSmall() {
            return small;
        }

        public String getPortrait() {
            return portrait;
        }

        public String getLandscape() {
            return landscape;
        }

        public String getTiny() {
            return tiny;
        }
    }
}
