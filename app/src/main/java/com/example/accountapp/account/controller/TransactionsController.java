package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.TransacationRequest;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.service.TransactionsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Transactions")
public class TransactionsController {
    private final TransactionsServiceImpl transactionsService;

    public TransactionsController(TransactionsServiceImpl transactionsService) {
        this.transactionsService = transactionsService;
    }
    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody TransacationRequest tr) {
        Account account = transactionsService.deposit(tr.getIdAccount(),tr.getAmount(),tr.getType(),tr.getIdUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
     @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody TransacationRequest tr) {
        Account account = transactionsService.withdraw(tr.getIdAccount(),tr.getAmount(),tr.getType(),tr.getIdUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
}
