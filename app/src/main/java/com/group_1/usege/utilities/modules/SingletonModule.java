package com.group_1.usege.utilities.modules;

import android.content.Context;

import com.group_1.usege.library.utilities.comparators.ImageComparator;
import com.group_1.usege.utilities.adapter.LoadStateAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SingletonModule {

    @Provides
    @Singleton
    public static LoadStateAdapter getLoadStateAdapter(@ApplicationContext Context context){
        return new LoadStateAdapter();
    }

    @Provides
    @Singleton
    public static ImageComparator getImageComparator(){
        return new ImageComparator();
    }
}

