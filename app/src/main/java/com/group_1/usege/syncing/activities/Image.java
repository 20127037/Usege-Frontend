package com.group_1.usege.syncing.activities;

public class Image {
    private String name;
    private String date;
    private Float size;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image(String name, String date, Float size, String description) {
        this.name = name;
        this.date = date;
        this.size = size;
        this.description = description;
    }
}
