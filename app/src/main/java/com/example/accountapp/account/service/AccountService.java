package com.example.accountapp.account.service;

import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();
    Optional<Account> getAccountById(Long id);
    Account createAccount(AccountRequestDTO dto);
    void deleteAccount(Long id);
    Account updateAccount(Long id, Account updatedAccount);
    Account deposit(Long id, double amount);
    Account withdraw(Long id, double amount);
    void transfer(Long fromAccountId, Long toAccountId, double amount);
    List<Transaction> getAccountTransactions(Long id);
    

}
