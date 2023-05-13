package com.group_1.usege.account.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;

import com.group_1.usege.R;
import com.group_1.usege.account.dto.CreateAccountRequestDto;
import com.group_1.usege.account.services.AccountServiceGenerator;
import com.group_1.usege.authen.activities.LoginActivity;
import com.group_1.usege.userInfo.model.UserInfo;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.ApiCallerActivity;
import com.group_1.usege.utilities.api.ResponseMessages;
import com.group_1.usege.utilities.dto.ErrorResponse;
import com.group_1.usege.utilities.view.EditTextFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAccountActivity extends ApiCallerActivity<UserInfo> {

    @Inject
    public AccountServiceGenerator accountServiceGenerator;
    private EditTextFragment fragEmail, fragPassword, fragRetype;

    public CreateAccountActivity() {
        super(R.layout.activity_create_account);
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
        fragRetype = (EditTextFragment)fragmentManager.findFragmentById(R.id.frag_retype);
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> signUp());
        Button btnSignUp = findViewById(R.id.btn_login);
        btnSignUp.setOnClickListener(v -> ActivityUtilities.TransitActivityAndFinish(this, LoginActivity.class));
    }

    private void handleWrongPasswordAndRetypeNotSame()
    {
        fragRetype.setError(R.string.password_and_retype_not_same);
    }

    private void handleEmailExists()
    {
        fragEmail.setError(R.string.email_has_already_exists);
    }

    private void handlePasswordNotFollowRules()
    {
        fragPassword.setError(R.string.password_not_follow_rules);
    }

    private void signUp(String email, String password)
    {
        startCallApi(accountServiceGenerator.getService()
                .create(new CreateAccountRequestDto(email, password)));
    }

    private void signUp()
    {
        //Make sure all of the input are not empty
        String email = fragEmail.getValue();
        if (email == null)
            return;
        String password = fragPassword.getValue();
        if (password == null)
            return;
        String retype = fragRetype.getValue();
        if (retype == null)
            return;
        if (!password.equals(retype))
        {
            handleWrongPasswordAndRetypeNotSame();
            return;
        }
        signUp(email, password);
    }

    @Override
    protected void handleCallSuccess(UserInfo userInfo) {
        Bundle infoBundle = new Bundle();
        infoBundle.putString(ResponseMessages.USERNAME, userInfo.getEmail());
        ActivityUtilities.TransitActivityAndFinish(this, ConfirmAccountActivity.class, infoBundle);
    }

    @Override
    protected void handleCallFail(ErrorResponse errorResponse) {
        if (errorResponse.getMessage() == null)
        {
            setCallApiFail();
            return;
        }
        switch (errorResponse.getMessage())
        {
            case ResponseMessages.USERNAME_EXISTS:
                handleEmailExists();
                break;
            case ResponseMessages.PASSWORD_INVALID:
                handlePasswordNotFollowRules();
                break;
            default:
                setCallApiFail();
                break;
        }
    }
}