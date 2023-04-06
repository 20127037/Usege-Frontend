package com.group_1.usege.account.activities;

import android.os.Bundle;
import android.widget.Button;

import com.group_1.usege.R;
import com.group_1.usege.account.services.AccountServiceGenerator;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.ApiCallerActivity;
import com.group_1.usege.utilities.api.ResponseMessages;
import com.group_1.usege.utilities.dto.ErrorResponse;
import com.group_1.usege.utilities.view.DialogueUtilities;
import com.group_1.usege.utilities.view.EditTextFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ResetPasswordActivity extends ApiCallerActivity<Void> {

    private EditTextFragment fragEmail;
    @Inject
    public AccountServiceGenerator accountServiceGenerator;
    private String email;

    public ResetPasswordActivity() {
        super(R.layout.activity_reset_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //confirmCodeFragment = (ConfirmCodeFragment) getSupportFragmentManager().findFragmentById(R.id.frag_confirm_code);
        fragEmail = (EditTextFragment)getSupportFragmentManager().findFragmentById(R.id.frag_email);
        Button submitBtn = findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(v -> resetPassword());
    }
    private void resetPassword()
    {
        email = fragEmail.getValue();
        if (email == null)
            return;
        resetPassword(email);
    }

    private void resetPassword(String email)
    {
        startCallApi(accountServiceGenerator.getService().forgetPassword(email));
    }

    @Override
    protected void handleCallSuccess(Void body) {
        Bundle infoBundle = new Bundle();
        infoBundle.putString(ResponseMessages.USERNAME, email);
        ActivityUtilities.TransitActivityAndFinish(this, ConfirmResetPassword.class, infoBundle);
    }

    private void handleUserNotFound()
    {
        fragEmail.setError(R.string.user_not_found);
    }

    @Override
    protected void handleCallFail(ErrorResponse errorResponse) {
        switch (errorResponse.getMessage())
        {
            case ResponseMessages.USER_NOT_FOUND:
                handleUserNotFound();
                return;
            case ResponseMessages.USER_NOT_CONFIRMED:
                DialogueUtilities.showConfirmDialogue(this, R.string.need_confirm_account, (v, w) -> {
                    Bundle infoBundle = new Bundle();
                    infoBundle.putString(ResponseMessages.USERNAME, email);
                    infoBundle.putBoolean(ConfirmAccountActivity.RESEND_IMMEDIATELY, true);
                    ActivityUtilities.TransitActivityAndFinish(this, ConfirmAccountActivity.class, infoBundle);
                },null);
                return;
            default:
                setCallApiFail();
        }
    }
}