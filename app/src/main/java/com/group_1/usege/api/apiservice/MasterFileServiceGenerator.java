package com.group_1.usege.api.apiservice;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class MasterFileServiceGenerator extends BaseServiceGenerator<MasterFileService> {

    @Inject
    MasterFileServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_master_file, "http://10.0.2.2:8090/api");
    }

    @Override
    protected Class<MasterFileService> getServiceClass() {
        return MasterFileService.class;
    }
}
