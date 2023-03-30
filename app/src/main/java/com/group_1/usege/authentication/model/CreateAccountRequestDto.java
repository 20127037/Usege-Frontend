package com.group_1.usege.authentication.model;

public class CreateAccountRequestDto {

    private final String email;
    private final String password;

    public CreateAccountRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
