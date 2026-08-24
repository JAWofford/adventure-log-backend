package com.jwofford.adventure_log_backend.exceptions;

public class DuplicateUserInfoException extends RuntimeException {

   /* public enum DuplicateField{
        USERNAME,
        EMAIL
    }*/

    public DuplicateUserInfoException(String type, String record) {
        super("The " + type + " " + record + " is already in use. Please choose a different " + type + ".");
    }
}
