package com.group_1.usege.library.service;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.utilities.api.BaseAuthServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class MasterAlbumServiceGenerator extends BaseAuthServiceGenerator<MasterAlbumService> {
    @Inject
    public MasterAlbumServiceGenerator(@ApplicationContext Context context, TokenRepository tokenRepository) {
        super(tokenRepository, context.getResources(), R.string.api_version, R.string.service_master_album);
    }

    @Override
    protected Class<MasterAlbumService> getServiceClass() {
        return MasterAlbumService.class;
    }
}
