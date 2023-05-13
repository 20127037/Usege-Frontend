package com.group_1.usege.authen.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;

import com.group_1.usege.R;
import com.group_1.usege.account.activities.ConfirmAccountActivity;
import com.group_1.usege.account.activities.CreateAccountActivity;
import com.group_1.usege.account.activities.ResetPasswordActivity;
import com.group_1.usege.account.dto.CreateAccountRequestDto;
import com.group_1.usege.authen.model.CacheToken;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.authen.services.AuthServiceGenerator;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.MasterUserServiceGenerator;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.ApiCallerActivity;
import com.group_1.usege.utilities.api.ResponseMessages;
import com.group_1.usege.utilities.dto.ErrorResponse;
import com.group_1.usege.utilities.view.DialogueUtilities;
import com.group_1.usege.utilities.view.EditTextFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginActivity extends ApiCallerActivity<CacheToken> {

    private EditTextFragment fragEmail, fragPassword;
    @Inject
    public TokenRepository tokenRepository;
    @Inject
    public AuthServiceGenerator authServiceGenerator;
    @Inject
    public UserInfoRepository userInfoRepository;
    @Inject
    public MasterUserServiceGenerator masterServiceGenerator;
    private String currentEmail;

    public LoginActivity() {
        super(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignLayoutView();
        checkHasLoggedIn();
    }

    private void assignLayoutView()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragEmail = (EditTextFragment)fragmentManager.findFragmentById(R.id.frag_email);
        fragPassword = (EditTextFragment)fragmentManager.findFragmentById(R.id.frag_password);
        Button btnSignIn = findViewById(R.id.btn_submit);
        btnSignIn.setOnClickListener(v -> signIn());
        Button toSignUpScreenBtn = findViewById(R.id.btn_sign_up);
        toSignUpScreenBtn.setOnClickListener(v -> ActivityUtilities.TransitActivity(this, CreateAccountActivity.class));
        Button toForgetPasswordScreen = findViewById(R.id.btn_forget_password);
        toForgetPasswordScreen.setOnClickListener(v -> ActivityUtilities.TransitActivity(this, ResetPasswordActivity.class));
    }

    private void signIn()
    {
        currentEmail = fragEmail.getValue();
        if (currentEmail == null)
            return;
        String password = fragPassword.getValue();
        if (password == null)
            return;
        signIn(currentEmail, password);
    }

    private void handleFailSignIn()
    {
        DialogueUtilities.showNormalDialogue(this, R.string.email_password_mismatch, null);
    }

    private void signIn(String email, String password)
    {
        startCallApi(authServiceGenerator
                .getService()
                .login(new CreateAccountRequestDto(email, password)));
    }

    @Override
    protected void handleCallSuccess(CacheToken result) {
        Log.i("Login", result.toString());
        tokenRepository.setToken(result);
//        ActivityUtilities.TransitActivityAndFinish(this, LibraryActivity.class);
        ActivityUtilities.TransitActivityAndFinish(this, LibraryActivity.class);
    }

    /**
     * If user has refresh token => try to get new access without inputting login account information
     */
    private void checkHasLoggedIn()
    {
        CacheToken cacheToken = tokenRepository.firstCheckToken();
        if (cacheToken == null)
            return;
        startCallApi(authServiceGenerator.getService()
                .refresh(cacheToken.getRefreshToken()));
    }

    protected void handleUserNotConfirmed() {
        DialogueUtilities.showConfirmDialogue(this, R.string.need_confirm_account, (v, w) -> {
            Bundle infoBundle = new Bundle();
            infoBundle.putString(ResponseMessages.USERNAME, currentEmail);
            infoBundle.putBoolean(ConfirmAccountActivity.RESEND_IMMEDIATELY, true);
            ActivityUtilities.TransitActivity(this, ConfirmAccountActivity.class, infoBundle);
        },null);
    }

    @Override
    protected void handleCallFail(ErrorResponse errorResponse) {
        if (errorResponse.getStatus() == 401)
            tokenRepository.setToken(null);
        if (errorResponse.getMessage() == null)
        {
            setCallApiFail();
            return;
        }
        switch (errorResponse.getMessage())
        {
            case ResponseMessages.USER_NOT_FOUND:
            case ResponseMessages.USERNAME_PASSWORD_MISMATCH:
                Log.e("Login", errorResponse.getMessage());
                handleFailSignIn();
                break;
            case ResponseMessages.USER_NOT_CONFIRMED:
                handleUserNotConfirmed();
                break;
            default:
                setCallApiFail();
                break;
        }
    }
}