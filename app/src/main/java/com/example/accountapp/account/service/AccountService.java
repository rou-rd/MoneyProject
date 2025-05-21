package com.example.accountapp.account.service;

import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();
    Optional<Account> getAccountById(Long id);
    Account createAccount(AccountRequestDTO dto);
    void deleteAccount(Long id);
    Account updateAccount(Long id, Account updatedAccount);

    

}
