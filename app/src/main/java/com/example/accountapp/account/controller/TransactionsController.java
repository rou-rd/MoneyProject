package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.TransacationRequest;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;
import com.example.accountapp.account.service.TransactionsServiceImpl;
import com.example.accountapp.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Transactions")
public class TransactionsController {
    private final TransactionsServiceImpl transactionsService;

    public TransactionsController(TransactionsServiceImpl transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<Account>> deposit(@RequestBody TransacationRequest tr) {
        try {
            Account account = transactionsService.deposit(tr.getIdAccount(), tr.getAmount(), tr.getType(), tr.getIdUser(), tr.getCategoryId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Deposit completed successfully", account));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Deposit failed", e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Account>> withdraw(@RequestBody TransacationRequest tr) {
        try {
            Account account = transactionsService.withdraw(tr.getIdAccount(), tr.getAmount(), tr.getType(), tr.getIdUser(), tr.getCategoryId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Withdrawal completed successfully", account));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Insufficient balance", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Withdrawal failed", e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<String>> transfer(@RequestBody TransacationRequest tr) {
        try {
            transactionsService.transfer(tr.getFromAccountId(), tr.getToAccountId(), tr.getAmount(), tr.getIdUser());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Transfer completed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Transfer failed", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Transfer failed", e.getMessage()));
        }
    }

    @GetMapping("/transactionsbyaccount/{id}")
    public ResponseEntity<ApiResponse<List<Transactions>>> getAccountTransactions(@PathVariable Long id) {
        try {
            List<Transactions> transactions = transactionsService.getAccountTransactions(id);
            if (transactions.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("No transactions found for this account", transactions));
            }
            return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve transactions", e.getMessage()));
        }
    }
}