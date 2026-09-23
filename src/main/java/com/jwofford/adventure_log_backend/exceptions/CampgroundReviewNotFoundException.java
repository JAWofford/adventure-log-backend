package com.jwofford.adventure_log_backend.exceptions;

public class CampgroundReviewNotFoundException extends RuntimeException {
    public CampgroundReviewNotFoundException(String message) {
        super(message);
    }
}
