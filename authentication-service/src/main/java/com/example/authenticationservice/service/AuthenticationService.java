package com.example.authenticationservice.service;

import com.example.authenticationservice.dao.request.RefreshTokenRequest;
import com.example.authenticationservice.dao.request.SignInRequest;
import com.example.authenticationservice.dao.request.SignUpRequest;
import com.example.authenticationservice.dao.response.JwtAuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthenticationService {
    ResponseEntity<Object> signup(SignUpRequest request);
    ResponseEntity<Object> signin(SignInRequest request);
    void validateToken(String token);
    void refreshToken(HttpServletRequest request,HttpServletResponse response) throws IOException;
}