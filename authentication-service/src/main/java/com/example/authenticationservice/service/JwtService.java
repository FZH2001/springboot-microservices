package com.example.authenticationservice.service;

import com.example.authenticationservice.model.User;
import org.springframework.security.core.userdetails.UserDetails;

//a custom service utilized for handling JWT operations

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);
    void validateToken(String token);
}