package com.group_1.usege.library.service;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.ViewModelScoped;

@ActivityScoped
public class PexelsServiceGenerator extends BaseServiceGenerator<PexelsService> {
    @Inject
    PexelsServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.domain_pexels_api);
    }

    @Override
    protected Class<PexelsService> getServiceClass() {
        return PexelsService.class;
    }
}
