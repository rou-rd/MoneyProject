package com.example.accountapp.security.dto;

public class RegisterRequest {

    private String username;
    private String password;
    private String email;

    private Long roleid;

    public Long getRoleid() {
        return roleid;
    }

    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }


    public RegisterRequest(String username, String password, String email, Long roleid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleid = roleid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
