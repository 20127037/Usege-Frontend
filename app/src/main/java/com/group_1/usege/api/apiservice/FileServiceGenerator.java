package com.group_1.usege.api.apiservice;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.utilities.api.BaseAuthServiceGenerator;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class FileServiceGenerator extends BaseAuthServiceGenerator<ApiService> {
    @Inject
    FileServiceGenerator(@ApplicationContext Context context, TokenRepository tokenRepository) {
        super(tokenRepository, context.getResources(), R.string.api_version, R.string.service_file);
    }

    @Override
    protected Class<ApiService> getServiceClass() {
        return ApiService.class;
    }
}
