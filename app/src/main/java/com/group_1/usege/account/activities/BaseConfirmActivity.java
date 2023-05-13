package com.group_1.usege.account.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.LayoutRes;

import com.group_1.usege.R;
import com.group_1.usege.account.fragments.CodeResend;
import com.group_1.usege.account.fragments.ConfirmCodeFragment;
import com.group_1.usege.authen.activities.LoginActivity;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.ApiCallerActivity;
import com.group_1.usege.utilities.api.ResponseMessages;
import com.group_1.usege.utilities.dto.ErrorResponse;
import com.group_1.usege.utilities.view.DialogueUtilities;

public abstract class BaseConfirmActivity extends ApiCallerActivity<Void> implements CodeResend {

    public static final String RESEND_IMMEDIATELY = "RESEND_IMMEDIATELY";
    protected ConfirmCodeFragment confirmCodeFragment;
    protected String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        confirmCodeFragment = (ConfirmCodeFragment) getSupportFragmentManager().findFragmentById(R.id.frag_confirm_code);
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> confirm());
    }

    public void onResume()
    {
        super.onResume();
        Bundle tranBundle = getIntent().getBundleExtra(ActivityUtilities.TRAN_ACT_BUNDLE);
        if (tranBundle != null)
        {
            username = tranBundle.getString(ResponseMessages.USERNAME);
            boolean resend = tranBundle.getBoolean(RESEND_IMMEDIATELY, false);
            if (resend)
                resend();
        }
    }

    public void confirm()
    {
        String code = confirmCodeFragment.getInput();
        if (code == null)
            return;
        confirm(code);
    }

    public abstract void confirm(String code);

    public BaseConfirmActivity(@LayoutRes int contentLayoutId)
    {
        super(contentLayoutId);
    }
    private void handleCodeMismatch() {
        Log.e("Confirm account", "Code mismatch");
        DialogueUtilities.showNormalDialogue(this, R.string.incorrect_confirm_code, (dialog, which) -> confirmCodeFragment.clear());
    }

    private void handleCodeExpired() {
        Log.e("Confirm account", "Code expired");
        DialogueUtilities.showNormalDialogue(this, R.string.expired_confirm_code, (dialog, which) -> ActivityUtilities.TransitActivityAndFinish(this, LoginActivity.class));
    }

    @Override
    protected void handleCallFail(ErrorResponse errorResponse) {
        if (errorResponse.getMessage() == null)
        {
            setCallApiFail();
            return;
        }
        switch (errorResponse.getMessage()) {
            case ResponseMessages.CODE_MISMATCH:
                handleCodeMismatch();
                break;
            case ResponseMessages.CODE_EXPIRED:
                handleCodeExpired();
                break;
            default:
                setCallApiFail();
                break;
        }
    }
}
