package com.group_1.usege.utilities.activities;

import com.group_1.usege.authen.activities.LoginActivity;
import com.group_1.usege.authen.model.CacheToken;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.authen.services.AuthServiceGenerator;
import com.group_1.usege.utilities.api.ResponseMessages;
import com.group_1.usege.utilities.dto.ErrorResponse;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

public abstract class AuthApiCallerActivity<S> extends ApiCallerActivity<S> {

    @Inject
    public TokenRepository tokenRepository;
    @Inject
    public AuthServiceGenerator authServiceGenerator;
    private Runnable previousCall;

    public AuthApiCallerActivity(int contentLayoutId) {
        super(contentLayoutId);
    }
    protected void tryRefreshToken()
    {
        CacheToken currentToken = tokenRepository.getToken();
        if (currentToken == null)
            onUnauthorized();
        authServiceGenerator
                .getService()
                .refresh(currentToken.getRefreshToken())
                .observeOn(AndroidSchedulers.mainThread())
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe(this::handleAfterRefresh);
    }

    private void handleAfterRefresh(Response<CacheToken> response, Throwable throwable) {
        if (throwable != null)
            onUnauthorized();
        else
        {
            if (response.isSuccessful())
            {
                if (previousCall != null)
                    previousCall.run();
            }
            else
                onUnauthorized();
        }
        previousCall = null;
    }

    protected final void startCallApi(Single<Response<S>> provider)
    {
        previousCall = () -> super.startCallApi(provider);
        previousCall.run();
    }

    protected final void startCallApiSilent(Single<Response<S>> provider)
    {
        previousCall = () -> super.startCallApiSilent(provider);
        previousCall.run();
    }

    /**
     * Called when the user is no longer authenticated
     */
    protected void onUnauthorized()
    {
        ActivityUtilities.TransitActivityAndFinish(this, LoginActivity.class);
    }
    @Override
    protected void handleCallFail(ErrorResponse errorResponse)
    {
        if (errorResponse.getStatus() == 401)
        {
            tryRefreshToken();
            return;
        }
        if (errorResponse.getMessage() == null)
        {
            setCallApiFail();
            return;
        }
        switch (errorResponse.getMessage())
        {
            case ResponseMessages.TOKEN_EXPIRED:
                tryRefreshToken();
                break;
            case ResponseMessages.UNAUTHORIZED:
                onUnauthorized();
                break;
        }
    }
}
