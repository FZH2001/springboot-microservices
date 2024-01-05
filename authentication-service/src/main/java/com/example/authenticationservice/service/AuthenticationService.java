package com.example.authenticationservice.service;

import com.example.authenticationservice.dao.request.SignInRequest;
import com.example.authenticationservice.dao.request.SignUpRequest;
import com.example.authenticationservice.dao.response.JwtAuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}