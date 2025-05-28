package com.example.accountapp.security.service;

import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.dto.RoleRequest;
import com.example.accountapp.security.model.Role;

import java.util.Optional;

public interface RoleService {
    String registerRole(RoleRequest request);
    Optional<Role> getAccountById(Long id);
    void deleteRole(Long id);
    Role updateRole(Long id, RoleRequest request);
}
