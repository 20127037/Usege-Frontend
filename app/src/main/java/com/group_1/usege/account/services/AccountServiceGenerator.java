package com.group_1.usege.account.services;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class AccountServiceGenerator extends BaseServiceGenerator<AccountService> {

    @Inject
    public AccountServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_account);
    }
    @Override
    protected Class<AccountService> getServiceClass() {
        return AccountService.class;
    }
}

