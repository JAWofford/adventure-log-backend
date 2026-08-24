package com.jwofford.adventure_log_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotBlank(message = "Username is required and cannot be just empty spaces.")
    private String userName;

    @NotBlank(message = "Display name is required and cannot be just empty spaces.")
    private String displayName;

    @NotBlank(message = "Email is required and cannot be just empty spaces.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    @NotBlank(message = "Password is required and cannot be just empty spaces.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    public UserRegistrationDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
