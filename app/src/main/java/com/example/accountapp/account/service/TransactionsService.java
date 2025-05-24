package com.example.accountapp.account.service;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionsService {


    Account deposit(Long id, BigDecimal amount,String type, Long idUser);
    List<Transactions> getAccountTransactions(Long id);
   
    Account withdraw(Long id, BigDecimal amount,String type,Long idUser);
     /*
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);

    */
}
