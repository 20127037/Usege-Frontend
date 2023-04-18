package com.group_1.usege.userInfo.model;

public class UserStatistic {
    private long usedSpaceInKb;
    private long maxSpaceInKb;
    private int countImg;
    private int countAlbum;

    public UserStatistic(long usedSpaceInKb, long maxSpaceInKb, int countImg, int countAlbum) {
        this.usedSpaceInKb = usedSpaceInKb;
        this.maxSpaceInKb = maxSpaceInKb;
        this.countImg = countImg;
        this.countAlbum = countAlbum;
    }

    public long getUsedSpaceInKb() {
        return usedSpaceInKb;
    }

    public void setUsedSpaceInKb(long usedSpaceInKb) {
        this.usedSpaceInKb = usedSpaceInKb;
    }

    public long getMaxSpaceInKb() {
        return maxSpaceInKb;
    }

    public void setMaxSpaceInKb(long maxSpaceInKb) {
        this.maxSpaceInKb = maxSpaceInKb;
    }

    public int getCountImg() {
        return countImg;
    }

    public void setCountImg(int countImg) {
        this.countImg = countImg;
    }

    public int getCountAlbum() {
        return countAlbum;
    }

    public void setCountAlbum(int countAlbum) {
        this.countAlbum = countAlbum;
    }
}