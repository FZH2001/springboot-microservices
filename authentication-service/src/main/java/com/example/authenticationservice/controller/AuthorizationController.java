package com.example.authenticationservice.controller;

import com.example.authenticationservice.model.Token;
import com.example.authenticationservice.model.User;
import com.example.authenticationservice.repository.TokenRepository;
import com.example.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource")

@CrossOrigin(origins = {"http://localhost:4200","https://main--remarkable-starlight-5f7dc0.netlify.app","https://main--unique-moxie-e9385c.netlify.app/","https://659d28735cac669fa793e38d--chic-mousse-aa1b3d.netlify.app/"})


@RequiredArgsConstructor
public class AuthorizationController {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("tokens")
    public ResponseEntity<List<Token>> getTokens() {
        return ResponseEntity.ok(tokenRepository.findAll());
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }


}