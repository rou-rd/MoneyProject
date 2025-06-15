package com.example.accountapp.security.controller;

import com.example.accountapp.common.Status;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.model.VerificationToken;
import com.example.accountapp.security.repository.UserRepository;
import com.example.accountapp.security.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class VerificationController {

    @Autowired
    private VerificationTokenRepository tokenRepo;
    @Autowired private UserRepository userRepo;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String token) {
        VerificationToken vt = tokenRepo.findByToken(token);
        if (vt == null || vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Invalid or expired token.";
        }

        User user = vt.getUser();
        user.setStatus(Status.INFORCE);
        userRepo.save(user);

        tokenRepo.delete(vt);
        return "Account verified successfully!";
    }
}