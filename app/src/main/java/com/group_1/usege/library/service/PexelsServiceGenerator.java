package com.group_1.usege.library.service;

import android.content.Context;
import android.content.res.Resources;

import com.group_1.usege.R;
import com.group_1.usege.library.model.PexelsPageResponse;
import com.group_1.usege.library.paging.PagingProvider;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ViewModelScoped;
import io.reactivex.rxjava3.core.Single;

@ViewModelScoped
public class PexelsServiceGenerator extends BaseServiceGenerator<PexelsService> {
    @Inject
    PexelsServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.domain_pexels_api, R.string.api_version, R.string.service_master);
    }

    @Override
    protected Class<PexelsService> getServiceClass() {
        return PexelsService.class;
    }
}
