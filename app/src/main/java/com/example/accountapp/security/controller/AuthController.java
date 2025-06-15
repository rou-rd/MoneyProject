package com.example.accountapp.security.controller;

import com.example.accountapp.security.dto.AuthRequest;
import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.service.UserServiceImpl;
import com.example.accountapp.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request) {
        try {
            String result = userServiceImpl.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully! Please check your email for verification.", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Registration failed", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthRequest request) {
        try {
            String token = userServiceImpl.authenticateUser(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Login failed", e.getMessage()));
        }
    }
}