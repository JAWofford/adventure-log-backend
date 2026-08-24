package com.jwofford.adventure_log_backend.dtos;

public class ErrorResponseDto {

    //backend will return this object in the response body when an error occurs
    //this object will contain a message describing the error which will be displayed on the frontend.
    //this message is not mutable.
    private final String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
