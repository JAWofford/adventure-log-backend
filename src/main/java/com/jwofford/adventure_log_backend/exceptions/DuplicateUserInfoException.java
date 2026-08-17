package com.jwofford.adventure_log_backend.exceptions;

public class DuplicateUserInfoException extends RuntimeException {

    public enum DuplicateField{
        USERNAME,
        EMAIL
    }

    public DuplicateUserInfoException(DuplicateField field) {
        super("That " + field.name().toLowerCase() + " is already in use. Please choose a different " + field.name().toLowerCase()+ ".");
    }
}
