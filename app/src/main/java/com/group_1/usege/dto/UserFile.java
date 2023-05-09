package com.group_1.usege.dto;

import java.util.List;
import java.util.Set;


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSizeInKb() {
        return sizeInKb;
    }

    public void setSizeInKb(Long sizeInKb) {
        this.sizeInKb = sizeInKb;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNormalUri() {
        return normalUri;
    }

    public void setNormalUri(String normalUri) {
        this.normalUri = normalUri;
    }

    public String getTinyUri() {
        return tinyUri;
    }

    public void setTinyUri(String tinyUri) {
        this.tinyUri = tinyUri;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public Integer getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }

    public Set<String> getPreviousAlbums() {
        return previousAlbums;
    }

    public void setPreviousAlbums(Set<String> previousAlbums) {
        this.previousAlbums = previousAlbums;
    }
}
