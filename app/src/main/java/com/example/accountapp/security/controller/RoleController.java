package com.example.accountapp.security.controller;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.security.dto.RegisterRequest;
import com.example.accountapp.security.dto.RoleRequest;
import com.example.accountapp.security.model.Role;
import com.example.accountapp.security.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roles")
public class RoleController {
    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @Autowired
    private RoleServiceImpl roleService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.registerRole(request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getAccountById(id)
                .map(account -> ResponseEntity.ok(account))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();  // Retourne un status 204 (No Content)
    }
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateAccount(@PathVariable Long id, @RequestBody RoleRequest role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
