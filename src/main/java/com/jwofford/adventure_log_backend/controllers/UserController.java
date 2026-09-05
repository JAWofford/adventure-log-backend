package com.jwofford.adventure_log_backend.controllers;


import com.jwofford.adventure_log_backend.dtos.UserLoginDto;
import com.jwofford.adventure_log_backend.dtos.UserRegistrationDto;
import com.jwofford.adventure_log_backend.dtos.AuthResponseDto;
import com.jwofford.adventure_log_backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    //helper function
    private Authentication establishSession(
            String username,
            String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        //package the credentials in the Authentication object Spring Expects. Implementation of Authentication interface
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        //Spring Security finds the user,checks the password, and either authenticates or rejects the login
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        // Create a place to store information about the user who was just authenticated
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        //put the authenticated user into "context"
        context.setAuthentication(authentication);

        // Tell Spring Security that this is the authenticated user for the current request
        SecurityContextHolder.setContext(context);

        //Save the authentication in the HTTP session so Spring Security can recognize this user on future requests
        securityContextRepository.saveContext(context, request, response);

        return  authentication;
    }

    @PostMapping("/auth/register")
    //use response entity to return 201 "created" status code.
    public ResponseEntity<AuthResponseDto> registerUser(
            @Valid @RequestBody UserRegistrationDto newUserRequest,
            HttpServletRequest request,
            HttpServletResponse response
            ) {
       AuthResponseDto authResponseDto = userService.registerNewUser(newUserRequest);

       establishSession(newUserRequest.getUserName(), newUserRequest.getPassword(), request, response);

       return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);

    }

    @PostMapping("/auth/login")
    //Receive the username and password from React and ask Spring Security to authenticate them
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody UserLoginDto loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication = establishSession(loginRequest.getUserName(), loginRequest.getPassword(), request, response);

        //Put together the information about our user that React needs.
        AuthResponseDto authResponseDto = userService.getAuthResponse(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        //check to see if a session was retrieved before invalidating
        if(session != null) {
            session.invalidate();
        }
        //clear auth info in Spring Security
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/whoami")
        public String whoami() {
         Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
        }
    }


