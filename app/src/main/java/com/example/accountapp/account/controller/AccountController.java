package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.service.AccountServiceImpl;
import com.example.accountapp.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Account")
public class AccountController {
    private final AccountServiceImpl service;

    public AccountController(AccountServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Account>>> getAllAccounts() {
        try {
            List<Account> accounts = service.getAllAccounts();
            return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accounts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve accounts", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Account>> getAccountById(@PathVariable Long id) {
        try {
            return service.getAccountById(id)
                    .map(account -> ResponseEntity.ok(ApiResponse.success("Account found", account)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Account not found with ID: " + id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve account", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Account>> createAccount(@RequestBody AccountRequestDTO dto) {
        try {
            Account newAccount = service.createAccount(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Account created successfully", newAccount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create account", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
        try {
            service.deleteAccount(id);
            return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete account", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Account>> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        try {
            Account updatedAccount = service.updateAccount(id, account);
            return ResponseEntity.ok(ApiResponse.success("Account updated successfully", updatedAccount));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Account not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update account", e.getMessage()));
        }
    }
}