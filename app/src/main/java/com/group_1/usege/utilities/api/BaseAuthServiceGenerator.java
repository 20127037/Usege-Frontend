package com.group_1.usege.utilities.api;

import android.content.res.Resources;

import androidx.annotation.StringRes;

import com.group_1.usege.authen.repository.TokenRepository;

public abstract class BaseAuthServiceGenerator<S> extends BaseServiceGenerator<S> {
    private final TokenRepository tokenRepository;

    public BaseAuthServiceGenerator(TokenRepository tokenRepository, Resources resources, @StringRes int versionRes, @StringRes int serviceNameRes) {
        super(resources, versionRes, serviceNameRes);
        this.tokenRepository = tokenRepository;
    }

    public BaseAuthServiceGenerator(TokenRepository tokenRepository, Resources resources, @StringRes int baseUrlResId) {
        super(resources, baseUrlResId);
        this.tokenRepository = tokenRepository;
    }

    public BaseAuthServiceGenerator(TokenRepository tokenRepository, Resources resources, @StringRes int domainRes, @StringRes int versionRes, @StringRes int serviceNameRes) {
        super(resources, domainRes, versionRes, serviceNameRes);
        this.tokenRepository = tokenRepository;
    }

    public synchronized S getService() {
        return super.getService(tokenRepository.getToken().getBearerAccessToken());
    }
}
