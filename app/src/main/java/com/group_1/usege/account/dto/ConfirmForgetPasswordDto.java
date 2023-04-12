package com.group_1.usege.account.dto;

public class ConfirmForgetPasswordDto {

    private String confirmCode;
    private String newPassword;

    public ConfirmForgetPasswordDto(String confirmCode, String newPassword) {
        this.confirmCode = confirmCode;
        this.newPassword = newPassword;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
