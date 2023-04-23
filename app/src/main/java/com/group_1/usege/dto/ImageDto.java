package com.group_1.usege.dto;

import android.net.Uri;

import java.util.List;

public class ImageDto {
    private String fileId;
    private List<String> tags;
    private String description;
    private String date;
    private long size;
    private String location;
    private String uri;

    public String getId() {
        return fileId;
    }

    public void setId(String id) {
        this.fileId = id;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ImageDto() {
    }

    public ImageDto(String fileId, List<String> tags, String description, String date, long size, String location, String uri) {
        this.fileId = fileId;
        this.tags = tags;
        this.description = description;
        this.date = date;
        this.size = size;
        this.location = location;
        this.uri = uri;
    }
}
