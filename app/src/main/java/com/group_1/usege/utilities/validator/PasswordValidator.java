package com.group_1.usege.utilities.validator;

import java.util.regex.Pattern;


public class PasswordValidator {
    private static final String passwordReg = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*\\d)(?=.*[a-z]).{8,}$";
    private static final Pattern pattern = Pattern.compile(passwordReg);
    public static boolean checkPasswordFollowRules(String password)
    {
        return pattern.matcher(password).matches();
    }
}
