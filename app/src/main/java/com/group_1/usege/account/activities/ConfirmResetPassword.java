package com.group_1.usege.account.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.group_1.usege.R;
import com.group_1.usege.account.dto.ConfirmForgetPasswordDto;
import com.group_1.usege.account.services.AccountServiceGenerator;
import com.group_1.usege.authen.activities.LoginActivity;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.view.DialogueUtilities;
import com.group_1.usege.utilities.view.EditTextFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConfirmResetPassword extends BaseConfirmActivity {

    private EditTextFragment fragPassword, fragRetype;
    @Inject
    public AccountServiceGenerator accountServiceGenerator;

    public ConfirmResetPassword() {
        super(R.layout.activity_confirm_reset_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragPassword = (EditTextFragment)fragmentManager.findFragmentById(R.id.frag_password);
        fragRetype = (EditTextFragment)fragmentManager.findFragmentById(R.id.frag_retype);
    }

    @Override
    public void confirm(String code) {
        String password = fragPassword.getValue();
        if (password == null)
            return;
        String retype = fragRetype.getValue();
        if (retype == null)
            return;
        if (!password.equals(retype))
        {
            fragRetype.setError(R.string.password_and_retype_not_same);
            return;
        }
        confirm(code, password);
    }

    private void confirm(String code, String password)
    {
        startCallApi(accountServiceGenerator.getService().confirmForgetPassword(username, new ConfirmForgetPasswordDto(code, password)));
    }

    @Override
    public void resend() {
        startCallApi(accountServiceGenerator.getService().resendForgetPasswordConfirmCode(username));
    }

    @Override
    public int getDelayTime() {
        return 60;
    }

    @Override
    protected void handleCallSuccess(Void body) {
        DialogueUtilities.showNormalDialogue(this, R.string.success_reset_password, (dialog, which) ->
                ActivityUtilities.TransitActivityAndFinish(this, LoginActivity.class));
    }
}