package com.group_1.usege.userInfo.repository;

import com.group_1.usege.userInfo.model.UserInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserInfoRepository {
    @Inject
    public UserInfoRepository()
    {

    }
    private UserInfo wrappedInfo;

    public void setInfo(UserInfo wrappedInfo) {
        this.wrappedInfo = wrappedInfo;
    }

    public UserInfo getInfo() {
        return wrappedInfo;
    }
}
