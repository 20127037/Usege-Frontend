package com.group_1.usege.album.services;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class AlbumServiceGenerator extends BaseServiceGenerator<AlbumService> {

    @Inject
    public AlbumServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_album);
    }
    @Override
    protected Class<AlbumService> getServiceClass() {
        return AlbumService.class;
    }
}

