package com.group_1.usege.utilities.modules;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.group_1.usege.R;
import com.group_1.usege.library.utilities.ImageComparator;
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
    public RequestManager getGlide(@ApplicationContext Context context){
        return Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .error(R.drawable.ic_error)
                        .placeholder(R.drawable.ic_cloud_download)
        );
    }

    @Provides
    @Singleton
    public LoadStateAdapter getLoadStateAdapter(@ApplicationContext Context context){
        return new LoadStateAdapter();
    }

    @Provides
    @Singleton
    public ImageComparator getImageComparator(){
        return new ImageComparator();
    }
}

