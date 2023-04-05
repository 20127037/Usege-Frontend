package com.group_1.usege.utilities.modules;

import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.utilities.parser.JsonParser;
import com.group_1.usege.utilities.view.BusyHandingProgressManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SingletonModule {
    @Provides
    public static BusyHandingProgressManager provideBusyHandingProgressManager()
    {
        return new BusyHandingProgressManager();
    }
    @Provides
    public static JsonParser provideJsonParser()
    {
        return new JsonParser();
    }
    @Provides
    public static UserInfoRepository provideUserInfoRepository()
    {
        return new UserInfoRepository();
    }
//    @Provides
//    public static TokenRepository provideTokenRepository()
//    {
//        return new TokenRepository();
//    }
}

