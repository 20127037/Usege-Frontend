package com.group_1.usege.authentication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.result.step.AuthNextSignInStep;
import com.amplifyframework.rx.RxAmplify;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.group_1.usege.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignLayoutView();
        initCognito();
    }

    private void assignLayoutView()
    {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnConfirmSignIn);
        btnSignIn.setOnClickListener(v -> signIn());
    }

    private void initCognito()
    {
        try {
            RxAmplify.addPlugin(new AWSCognitoAuthPlugin());
            RxAmplify.configure(getApplicationContext());
        } catch (AmplifyException e) {
            throw new RuntimeException(e);
        }
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

    private void onDoneSignIn()
    {

    }

    @SuppressLint("CheckResult")
    private void signIn(String email, String password)
    {
        RxAmplify.Auth.signIn(email, password)
                .subscribe(
                        result -> {
                            AuthNextSignInStep nextStep = result.getNextStep();
                            switch (nextStep.getSignInStep()) {
                                case CONFIRM_SIGN_UP: {
                                    Log.i("AuthQuickstart", "Confirm signup, additional info: " + nextStep.getAdditionalInfo());
                                    break;
                                }
                                case DONE: {
                                    Log.i("Auth done", result.toString());
                                    break;
                                }
                            }

                        },
                        error -> Log.e("AmplifyQuickstart", error.toString()));
    }
}