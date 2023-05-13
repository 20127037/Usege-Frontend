package com.group_1.usege.userInfo.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.group_1.usege.R;
import com.group_1.usege.authen.activities.LoginActivity;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.userInfo.dtos.PaymentRequestDto;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.PaymentServiceGenerator;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.AuthApiCallerActivity;
import com.group_1.usege.utilities.api.ResponseMessages;
import com.group_1.usege.utilities.dto.ErrorResponse;
import com.group_1.usege.utilities.view.DialogueUtilities;
import com.group_1.usege.utilities.view.EditTextFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PaymentActivity extends AuthApiCallerActivity<Void> {

    @Inject
    public PaymentServiceGenerator paymentServiceGenerator;
    @Inject
    public TokenRepository tokenRepository;
    private EditTextFragment fragCardNumber, fragCvv, fragExpiredDate;
    private TextView txtPlanName, txtPrice;

    public PaymentActivity()
    {
        super(R.layout.activity_payment);
    }

    public static final String PLAN_NAME_KEY = "PLAN_NAME";
    public static final String PRICE_KEY = "PRICE";
    private String currentPlanName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        fragCardNumber = (EditTextFragment)fm.findFragmentById(R.id.frag_card_number);
        fragCvv = (EditTextFragment)fm.findFragmentById(R.id.frag_cvv);
        fragExpiredDate = (EditTextFragment)fm.findFragmentById(R.id.frag_expired_date);
        txtPrice = findViewById(R.id.txt_price);
        txtPlanName = findViewById(R.id.txt_plan);
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> payment());
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    public void onResume()
    {
        super.onResume();
        Bundle bundle = getIntent().getBundleExtra(ActivityUtilities.TRAN_ACT_BUNDLE);
        if (bundle != null)
        {
            currentPlanName = bundle.getString(PLAN_NAME_KEY);
            float price = bundle.getFloat(PRICE_KEY);
            txtPlanName.setText(getString(R.string.embedded_payment_plan, currentPlanName));
            txtPrice.setText(getString(R.string.embedded_payment_price, price));
        }
        else
            finish();
    }

    private void payment()
    {
        String cardNumber = fragCardNumber.getValue();
        if (cardNumber == null)
            return;
        String cvv = fragCvv.getValue();
        if (cvv == null)
            return;
        String expiredDate = fragExpiredDate.getValue();
        if (expiredDate == null)
            return;
        payment(cardNumber, cvv, expiredDate);
    }

    private void payment(String cardNumber, String cvv, String expiredDate)
    {
        startCallApi(paymentServiceGenerator.getService()
                .payment(tokenRepository.getToken().getUserId(),
                        new PaymentRequestDto(currentPlanName, cardNumber, cvv, expiredDate)));
    }
    @Override
    protected void handleCallFail(ErrorResponse errorResponse)
    {
        if (errorResponse.getMessage() != null)
        {
            switch (errorResponse.getMessage())
            {
                case ResponseMessages.INVALID_CARD:
                    DialogueUtilities.showNormalDialogue(this, R.string.invalid_card, null);
                    return;
                case ResponseMessages.INVALID_PLAN:
                    DialogueUtilities.showNormalDialogue(this, R.string.invalid_plan, (d, w) -> ActivityUtilities.TransitActivityAndFinish(this, LibraryActivity.class));
                    return;
            }
        }
        super.handleCallFail(errorResponse);
    }

    @Override
    protected void handleCallSuccess(Void body) {
        DialogueUtilities.showNormalDialogue(this, R.string.success_payment, (dialog, which) ->
                ActivityUtilities.TransitActivityAndFinish(this, LibraryActivity.class));
    }
}
