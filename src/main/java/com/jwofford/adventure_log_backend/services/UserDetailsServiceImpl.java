package com.jwofford.adventure_log_backend.services;

import com.jwofford.adventure_log_backend.models.User;
import com.jwofford.adventure_log_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        //look for user in database
        //if user exists, create a UserDetails object with info Spring Security needs for authentication
        //if not throw Spring Security exception as defined by interface.
        //build UserDetails object to return using Spring's User object builder.
        User user = userRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("User not found:" + username)); //this is the adventurelog User object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPasswordHash())
                .authorities("USER")
                .build();
    }


}
