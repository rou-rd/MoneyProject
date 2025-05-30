package com.example.accountapp.security.controller;

import com.example.accountapp.security.dto.AuthRequest;
import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class AuthController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userServiceImpl.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userServiceImpl.authenticateUser(request));
    }

}

