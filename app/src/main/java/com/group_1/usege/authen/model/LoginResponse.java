package com.group_1.usege.authen.model;

import com.group_1.usege.userInfo.model.UserInfo;

public class LoginResponse {
    private CacheToken token;
    private UserInfo userInfo;

    public CacheToken getToken() {
        return token;
    }

    public void setToken(CacheToken token) {
        this.token = token;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
