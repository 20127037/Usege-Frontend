package com.group_1.usege.authen.model;

import com.group_1.usege.userInfo.model.UserInfo;

public class LoginResponse {
    private CacheToken cacheToken;
    private UserInfo userInfo;

    public CacheToken getCacheToken() {
        return cacheToken;
    }

    public void setCacheToken(CacheToken cacheToken) {
        this.cacheToken = cacheToken;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
