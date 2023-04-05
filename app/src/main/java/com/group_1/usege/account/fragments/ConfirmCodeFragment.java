package com.group_1.usege.account.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodiebag.pinview.Pinview;
import com.group_1.usege.R;
import com.group_1.usege.utilities.view.TimerTextView;

public class ConfirmCodeFragment extends Fragment {

    private Pinview pinview;
    private Button btnResend;
    private TimerTextView timerTextView;
    private ViewGroup layoutDelay;
    //private Button btnSubmit;
    private CodeResend codeResend;

    public ConfirmCodeFragment() {
        super(R.layout.fragment_confirm_code);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CodeResend)
            this.codeResend = (CodeResend)context;
    }

    public String getInput()
    {
        String input = pinview.getValue();
        if (input.length() != pinview.getPinLength())
            return null;
        return input;
    }

    public void clear()
    {
        pinview.clearValue();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //btnSubmit = view.findViewById(R.id.btn_submit);
        pinview = view.findViewById(R.id.pinview);
        timerTextView = view.findViewById(R.id.text_timer);
        btnResend = view.findViewById(R.id.btn_resend);
        layoutDelay = view.findViewById(R.id.layout_delay);

        btnResend.setOnClickListener(v -> resendCode());
        //btnSubmit.setOnClickListener(v -> confirm());
    }

    private void resendCode()
    {
        codeResend.resend();
        startDelaying();
    }

    private void startDelaying()
    {
        btnResend.setVisibility(View.GONE);
        layoutDelay.setVisibility(View.VISIBLE);
        timerTextView.startDelaying(codeResend.getDelayTime(), () -> {
            btnResend.setVisibility(View.VISIBLE);
            layoutDelay.setVisibility(View.GONE);
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        layoutDelay.setVisibility(View.GONE);
//    }

    @Override
    public void onPause() {
        super.onPause();
        timerTextView.forceStop();
    }
}