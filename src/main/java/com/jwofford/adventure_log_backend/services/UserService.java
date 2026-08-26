package com.jwofford.adventure_log_backend.services;

import com.jwofford.adventure_log_backend.dtos.UserRegistrationDto;
import com.jwofford.adventure_log_backend.dtos.AuthResponseDto;
import com.jwofford.adventure_log_backend.exceptions.DuplicateUserInfoException;
import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //See SecurityConfig for type of encoder used.
    @Autowired
    private PasswordEncoder passwordEncoder;



    public AuthResponseDto registerNewUser (UserRegistrationDto user){

        //Does a user with that username already exist?
       Optional<User> existingUser = userRepository.findByUserName(user.getUserName());
       if (existingUser.isPresent()) {
            throw new DuplicateUserInfoException(DuplicateUserInfoException.DuplicateField.USERNAME);
        }
        //Does a user with that email already exist?
        Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            throw new DuplicateUserInfoException(DuplicateUserInfoException.DuplicateField.EMAIL);
        }
        //If both checks pass, hash the raw password
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        //Build a user object with the hashed password and set createdAt
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setDisplayName(user.getDisplayName());
        newUser.setEmail(user.getEmail());
        newUser.setPasswordHash(hashedPassword);
        newUser.setCreatedAt(LocalDateTime.now());

        //Save via the repository.
        User savedUser = userRepository.save(newUser);

        //Build and return a UserResponseDto with the saved user info (excluding passwordHash)
        return new AuthResponseDto(savedUser.getId(), savedUser.getDisplayName());
    }

}
