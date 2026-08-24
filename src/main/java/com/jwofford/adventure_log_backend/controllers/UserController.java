package com.jwofford.adventure_log_backend.controllers;


import com.jwofford.adventure_log_backend.dtos.UserRegistrationDto;
import com.jwofford.adventure_log_backend.dtos.AuthResponseDto;
import com.jwofford.adventure_log_backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    //constructor dependency injection
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    //use response entity to return 201 "created" status code.
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody UserRegistrationDto newUserRequest) {
       AuthResponseDto authResponseDto = userService.registerNewUser(newUserRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);

    }

}
