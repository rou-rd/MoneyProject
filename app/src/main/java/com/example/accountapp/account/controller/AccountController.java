package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.service.AccountServiceImpl;
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
    public List<Account> getAllAccounts() {
        return service.getAllAccounts();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return service.getAccountById(id)
                .map(account -> ResponseEntity.ok(account))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequestDTO dto) {
        Account newAccount = service.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        service.deleteAccount(id);
        return ResponseEntity.noContent().build();  // Retourne un status 204 (No Content)
    }
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        try {
            Account updatedAccount = service.updateAccount(id, account);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
