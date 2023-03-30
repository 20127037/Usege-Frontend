package com.group_1.usege.authentication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.group_1.usege.R;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAccountActivity extends AppCompatActivity {

    @Inject
    public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }
}