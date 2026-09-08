package com.jwofford.adventure_log_backend.exceptions;

public class TripLogNotFoundException extends RuntimeException {
    public TripLogNotFoundException(String message) {
        super(message);
    }
}
