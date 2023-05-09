package com.group_1.usege.api.apiservice;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.userInfo.services.MasterService;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class ApiServiceGenerator extends BaseServiceGenerator<ApiService> {
    @Inject
    ApiServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_file,"http://10.0.2.2:8083/api");
    }

    @Override
    protected Class<ApiService> getServiceClass() {
        return ApiService.class;
    }
}
