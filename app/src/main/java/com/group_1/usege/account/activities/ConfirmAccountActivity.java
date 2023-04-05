package com.group_1.usege.account.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.ContentView;

import com.group_1.usege.R;
import com.group_1.usege.account.fragments.ConfirmCodeFragment;
import com.group_1.usege.account.services.AccountService;
import com.group_1.usege.account.services.AccountServiceGenerator;
import com.group_1.usege.authen.activities.LoginActivity;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.view.DialogueUtilities;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConfirmAccountActivity extends BaseConfirmActivity {
    @Inject
    public AccountServiceGenerator accountServiceGenerator;

    public ConfirmAccountActivity()
    {
        super(R.layout.activity_confirm_account);
    }

    public void confirm(String code)
    {
        startCallApi(accountServiceGenerator.getService()
                .confirm(username, code));
    }

    @Override
    protected void handleCallSuccess(Void body) {
        Log.i("Confirm account", "Success");
        DialogueUtilities.showNormalDialogue(this, R.string.success_confirm_account, (dialog, which) ->
                ActivityUtilities.TransitActivityAndFinish(this, LoginActivity.class));
    }

    @Override
    public void resend() {
        startCallApi(accountServiceGenerator.getService().resendAccountConfirmCode(username));
    }

    @Override
    public int getDelayTime() {
        return 60;
    }
}