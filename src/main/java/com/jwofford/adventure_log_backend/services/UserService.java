package com.jwofford.adventure_log_backend.services;

import com.jwofford.adventure_log_backend.exceptions.DuplicateUserInfoException;
import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto registerNewUser (User user){

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

        //If both checks pass, hash the raw password (BCrypt?)
        //Build a user object with the hashed password and set createdAt
        //Save via the repository.
        //Return something.  The full User? Just the name?
    }

}
