package com.example.authenticationservice.service;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
//    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    }