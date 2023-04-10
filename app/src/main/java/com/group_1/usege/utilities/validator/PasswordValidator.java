package com.group_1.usege.utilities.validator;

import java.util.regex.Pattern;

import dagger.hilt.android.scopes.ActivityScoped;


@ActivityScoped
public class PasswordValidator {
    private static final String passwordReg = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*\\d)(?=.*[a-z]).{8,}$";
    private final Pattern pattern;
    public PasswordValidator()
    {
        pattern = Pattern.compile(passwordReg);
    }
    public boolean checkPasswordFollowRules(String password)
    {
        return pattern.matcher(password).matches();
    }
}
