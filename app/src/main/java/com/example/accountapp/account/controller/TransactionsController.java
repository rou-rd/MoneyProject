package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.TransacationRequest;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;
import com.example.accountapp.account.service.TransactionsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("Transactions")
public class TransactionsController {
    private final TransactionsServiceImpl transactionsService;

    public TransactionsController(TransactionsServiceImpl transactionsService) {
        this.transactionsService = transactionsService;
    }
    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody TransacationRequest tr) {
        Account account = transactionsService.deposit(tr.getIdAccount(),tr.getAmount(),tr.getType(),tr.getIdUser(),tr.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
     @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody TransacationRequest tr) {
        Account account = transactionsService.withdraw(tr.getIdAccount(),tr.getAmount(),tr.getType(),tr.getIdUser(),tr.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
    @PostMapping("/transfer")
    public ResponseEntity<TransacationRequest> transfet(@RequestBody TransacationRequest tr) {
        transactionsService.transfer(tr.getFromAccountId(),tr.getToAccountId(),tr.getAmount(),tr.getIdUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(tr);
    }
    @GetMapping("/transactionsbyaccount/{id}")
    public ResponseEntity<List<Transactions>> getAccountTransactions(@PathVariable Long id) {
        List<Transactions> transactions = transactionsService.getAccountTransactions(id);
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }
}
