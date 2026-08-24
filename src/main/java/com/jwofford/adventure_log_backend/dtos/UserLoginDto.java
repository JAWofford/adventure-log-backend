package com.jwofford.adventure_log_backend.dtos;

import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Password is required")
    private String password;

    public UserLoginDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
