package com.group_1.usege.utilities.modules;

import com.group_1.usege.utilities.validator.PasswordValidator;
import com.group_1.usege.utilities.view.BusyHandingProgressManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {
    @Provides
    public static PasswordValidator providePasswordValidator()
    {
        return new PasswordValidator();
    }
}
