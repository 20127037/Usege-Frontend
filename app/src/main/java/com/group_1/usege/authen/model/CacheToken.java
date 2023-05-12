package com.group_1.usege.authen.model;

public class CacheToken {
    private String userId;
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String idToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getBearerAccessToken()
    {
        return "Bearer " + accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
