package com.group_1.usege.library.service;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.utilities.api.BaseAuthServiceGenerator;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class MasterFileServiceGenerator extends BaseAuthServiceGenerator<MasterFileService> {
    @Inject
    public MasterFileServiceGenerator(@ApplicationContext Context context, TokenRepository tokenRepository) {
        super(tokenRepository, context.getResources(), R.string.api_version, R.string.service_master_file);
    }

    @Override
    protected Class<MasterFileService> getServiceClass() {
        return MasterFileService.class;
    }
}
