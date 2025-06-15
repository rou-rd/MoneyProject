package com.example.accountapp.security.controller;

import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.dto.RoleRequest;
import com.example.accountapp.security.model.Role;
import com.example.accountapp.security.service.RoleServiceImpl;
import com.example.accountapp.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RoleRequest request) {
        try {
            String result = roleService.registerRole(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Role created successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create role", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Long id) {
        try {
            return roleService.getAccountById(id)
                    .map(role -> ResponseEntity.ok(ApiResponse.success("Role found", role)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Role not found with ID: " + id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve role", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok(ApiResponse.success("Role deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete role", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> updateRole(@PathVariable Long id, @RequestBody RoleRequest role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(ApiResponse.success("Role updated successfully", updatedRole));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Role not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update role", e.getMessage()));
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<ApiResponse<List<RoleRequest>>> getAllRoles() {
        try {
            List<RoleRequest> roles = roleService.getAllRoles();
            return ResponseEntity.ok(ApiResponse.success("Roles retrieved successfully", roles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve roles", e.getMessage()));
        }
    }
}