package com.jwofford.adventure_log_backend.exceptions;

import com.jwofford.adventure_log_backend.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handle DuplicateUserInfoException
    @ExceptionHandler(DuplicateUserInfoException.class)

    public ResponseEntity<ErrorResponseDto> handleDuplicateUserInfoException(DuplicateUserInfoException ex) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }

    //Handle login failure.
    //Handle UsernameNotFoundException and BadCredentialsException by handling the parent AuthenticationException.
    @ExceptionHandler(AuthenticationException.class)

    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto("Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }

}
