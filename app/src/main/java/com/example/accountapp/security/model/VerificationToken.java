package com.example.accountapp.security.model;

import com.example.accountapp.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity
public class VerificationToken extends BaseEntity {

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @OneToOne
    private User user;

    private String token;

    private LocalDateTime expiryDate;

    public VerificationToken(User user, String token, LocalDateTime expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public VerificationToken() {
    }

    public VerificationToken(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, User user, String token, LocalDateTime expiryDate) {
        super(createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
