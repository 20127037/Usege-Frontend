package com.group_1.usege.library.service;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.utilities.api.BaseAuthServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class TrashServiceGenerator extends BaseAuthServiceGenerator<TrashService> {

    @Inject
    public TrashServiceGenerator(@ApplicationContext Context context, TokenRepository tokenRepository) {
        super(tokenRepository, context.getResources(), R.string.api_version, R.string.service_trash);
    }

    @Override
    protected Class<TrashService> getServiceClass() {
        return TrashService.class;
    }
}
