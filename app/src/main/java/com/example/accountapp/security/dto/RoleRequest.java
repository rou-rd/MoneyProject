package com.example.accountapp.security.dto;

public class RoleRequest {
    private Long userId;
    private String roleName;

    public RoleRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public RoleRequest(Long userId, String roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }
}
