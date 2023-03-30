package com.group_1.usege.authentication.services;

import android.content.Context;

import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class AccountServiceGenerator extends BaseServiceGenerator<AccountService> {
    @Inject
    AccountServiceGenerator(@ApplicationContext Context context) {
        super(context);
    }
    @Override
    protected Class<AccountService> getServiceClass() {
        return AccountService.class;
    }
}