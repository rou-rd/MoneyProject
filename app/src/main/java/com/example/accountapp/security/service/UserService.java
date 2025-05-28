package com.example.accountapp.security.service;

import com.example.accountapp.security.dto.AuthRequest;
import com.example.accountapp.security.dto.RegisterRequest;

public interface UserService {
    String registerUser(RegisterRequest request);
    String authenticateUser(AuthRequest request);
}
