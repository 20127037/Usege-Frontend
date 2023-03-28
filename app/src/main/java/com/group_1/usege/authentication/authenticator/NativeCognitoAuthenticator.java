package com.group_1.usege.authentication.authenticator;

import static com.amplifyframework.auth.result.step.AuthSignInStep.CONFIRM_SIGN_IN_WITH_SMS_MFA_CODE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.auth.result.step.AuthNextSignInStep;
import com.amplifyframework.rx.RxAmplify;
import com.group_1.usege.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class NativeCognitoAuthenticator {

    private final String USERNAME = "USERNAME";
    private final String PASSWORD = "PASSWORD";

    public NativeCognitoAuthenticator(Context context) {
        try {
            RxAmplify.addPlugin(new AWSCognitoAuthPlugin());
            RxAmplify.configure(context);
        } catch (AmplifyException e) {
            Log.e("Amplify", e.getMessage());
        }
    }

    @SuppressLint("CheckResult")
    public void signIn(String username,
                       String password,
                       Consumer<String> notConfirmed,
                       Consumer<String> success,
                       Consumer<String> fail) {
        RxAmplify.Auth.signIn(username, password)
                .subscribe(result ->
                        {
                            AuthNextSignInStep nextStep = result.getNextStep();
                            switch (nextStep.getSignInStep()) {
                                case CONFIRM_SIGN_UP: {
                                    notConfirmed.accept(nextStep.toString());
                                    break;
                                }
                                case DONE: {
                                    success.accept(nextStep.toString());
                                    break;
                                }
                                default: {
                                    fail.accept(nextStep.toString());
                                    break;
                                }
                            }
                        },
                        error -> fail.accept(error.getMessage()));
    }
}
