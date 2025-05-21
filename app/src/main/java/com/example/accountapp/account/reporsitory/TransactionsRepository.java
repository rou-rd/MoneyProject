package com.example.accountapp.account.reporsitory;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByAccount(Account account);
}
