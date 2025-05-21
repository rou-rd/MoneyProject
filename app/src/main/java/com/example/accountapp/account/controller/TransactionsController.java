package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.dto.TransacationRequest;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;
import com.example.accountapp.account.service.TransactionsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("Transactions")
public class TransactionsController {
    private final TransactionsServiceImpl transactionsService;

    public TransactionsController(TransactionsServiceImpl transactionsService) {
        this.transactionsService = transactionsService;
    }
    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody TransacationRequest tr) {
        Account account = transactionsService.deposit(tr.getId(),tr.getAmount(),tr.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
}
