package com.group_1.usege.authen.services;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

@Singleton
public class AuthServiceGenerator extends BaseServiceGenerator<AuthService> {

    @Inject
    public AuthServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_auth);
    }

    @Override
    protected Class<AuthService> getServiceClass() {
        return AuthService.class;
    }
}
