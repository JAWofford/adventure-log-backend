package com.jwofford.adventure_log_backend.exceptions;

public class DuplicateUserInfoException extends RuntimeException {

    //specific choices for the field that is duplicated, so that we can return a specific error message to the frontend.
    public enum DuplicateField{
        USERNAME,
        EMAIL
    }

    public DuplicateUserInfoException(DuplicateField field) {
        super(buildMessage(field));
    }
//error messaged for each field, no default needed with enum since all cases are covered.
    private static String buildMessage(DuplicateField field) {
        return switch (field) {
            case USERNAME -> "That username is already in use.  Please choose a different username.";
            case EMAIL -> "Unable to register user with the provided information";
        };
    }
}
