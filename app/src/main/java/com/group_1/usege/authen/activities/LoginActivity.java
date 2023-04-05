package com.group_1.usege.authen.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;

import com.group_1.usege.R;
import com.group_1.usege.account.activities.ConfirmAccountActivity;
import com.group_1.usege.account.activities.CreateAccountActivity;
import com.group_1.usege.account.dto.CreateAccountRequestDto;
import com.group_1.usege.authen.model.CacheToken;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.account.services.AccountService;
import com.group_1.usege.authen.services.AuthService;
import com.group_1.usege.authen.services.AuthServiceGenerator;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.ApiCallerActivity;
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
    private String currentEmail;

    public LoginActivity() {
        super(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignLayoutView();
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
    }

    protected void handleUserNotConfirmed() {
        Bundle infoBundle = new Bundle();
        infoBundle.putString(AccountService.USERNAME, currentEmail);
        ActivityUtilities.TransitActivity(this, ConfirmAccountActivity.class, infoBundle);
    }

    @Override
    protected void handleCallFail(ErrorResponse errorResponse) {
        switch (errorResponse.getMessage())
        {
            case AccountService.USER_NOT_FOUND:
            case AuthService.USERNAME_PASSWORD_MISMATCH:
                Log.e("Login", errorResponse.getMessage());
                handleFailSignIn();
                break;
            case AuthService.USER_NOT_CONFIRMED:
                handleUserNotConfirmed();
                break;
            default:
                setCallApiFail();
                break;
        }
    }
}