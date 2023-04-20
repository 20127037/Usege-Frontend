package com.group_1.usege.userInfo.services;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class PaymentServiceGenerator extends BaseServiceGenerator<PaymentService> {
    @Inject
    PaymentServiceGenerator(@ApplicationContext Context context) {
        super(context.getResources(), R.string.api_version, R.string.service_payment);
    }

    @Override
    protected Class<PaymentService> getServiceClass() {
        return PaymentService.class;
    }
}