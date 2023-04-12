package com.group_1.usege.userInfo.services;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class MasterServiceGenerator extends BaseServiceGenerator<MasterService> {
    @Inject
    MasterServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_master);
    }

    @Override
    protected Class<MasterService> getServiceClass() {
        return MasterService.class;
    }
}
