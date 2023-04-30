package com.group_1.usege.utilities.modules;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.group_1.usege.R;
import com.group_1.usege.library.service.PexelsServiceGenerator;
import com.group_1.usege.utilities.validator.PasswordValidator;
import com.group_1.usege.utilities.view.BusyHandingProgressManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {
    @Provides
    @SmallPlaceHolder
    @ActivityScoped
    public static RequestManager getSmallGlide(@ActivityContext Context context){
        return getGlide(context, 10f, 50f);
    }
    public static RequestManager getGlide(Context context, float with, float rad){
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(with);
        circularProgressDrawable.setCenterRadius(rad);
        circularProgressDrawable.start();
        return Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .error(R.drawable.ic_error)
                        .timeout(10000)
                        .placeholder(circularProgressDrawable)
        );
    }

    @Provides
    @BigPlaceHolder
    @ActivityScoped
    public static RequestManager getBigGlide(@ActivityContext Context context){
        return getGlide(context, 20f, 100);
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SmallPlaceHolder {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BigPlaceHolder {}
}

