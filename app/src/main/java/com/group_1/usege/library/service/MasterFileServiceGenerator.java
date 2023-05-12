package com.group_1.usege.library.service;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class MasterFileServiceGenerator extends BaseServiceGenerator<MasterFileService> {
    @Inject
    public MasterFileServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_master_file);
    }

    @Override
    protected Class<MasterFileService> getServiceClass() {
        return MasterFileService.class;
    }
}
