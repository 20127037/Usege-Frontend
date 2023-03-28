package com.group_1.usege.authentication.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.rx.RxAmplify;
import com.group_1.usege.R;
import com.group_1.usege.authentication.authenticator.NativeCognitoAuthenticator;


public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    private NativeCognitoAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authenticator = new NativeCognitoAuthenticator(this);
        assignLayoutView();
    }

    private void assignLayoutView()
    {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnConfirmSignIn);
        btnSignIn.setOnClickListener(v -> signIn());
    }


    private void signIn()
    {
        String email = edtEmail.getText().toString();
        if (email.isEmpty())
            return;
        String password = edtPassword.getText().toString();
        if (password.isEmpty())
            return;

        signIn(email, password);
    }

    @SuppressLint("CheckResult")
    private void onDoneSignIn(String result)
    {
        Log.i("Token", result);
        RxAmplify.Auth.getCurrentUser().subscribe(
                currentResult -> Log.i("AuthQuickStart getCurrentUser: ", currentResult.toString()),
                error -> Log.e("AuthQuickStart", error.toString())
        );
    }

    private void onFailSignIn(String message)
    {
        Log.e("Failed sign in", message);
    }

    private void signIn(String email, String password)
    {
        authenticator
                .signIn(email, password, this::onFailSignIn, this::onDoneSignIn, this::onFailSignIn);
    }
}