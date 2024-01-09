package com.example.authenticationservice.controller;

import com.example.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://localhost:4200","https://main--remarkable-starlight-5f7dc0.netlify.app","https://main--unique-moxie-e9385c.netlify.app/","https://659d28735cac669fa793e38d--chic-mousse-aa1b3d.netlify.app/"})

public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }


//    @PatchMapping("resetPassword")
//    public ResponseEntity<?> changePassword(
//            @RequestBody ChangePasswordRequest request,
//            Principal connectedUser
//    ) {
//        userService.changePassword(request, connectedUser);
//        return ResponseEntity.ok().build();
//    }
}