package com.example.accountapp.account.service;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;
import com.example.accountapp.account.reporsitory.AccountRepository;
import com.example.accountapp.account.reporsitory.TransactionsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final AccountRepository accountRepository;

    public TransactionsServiceImpl(TransactionsRepository transactionsRepository, AccountRepository accountRepository) {
        this.transactionsRepository = transactionsRepository;
        this.accountRepository = accountRepository;
    }

        @Override
        public Account deposit(Long id, BigDecimal amount,String type) {
            Account account = getAccountOrThrow(id);

            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);

            transactionsRepository.save(new Transactions(LocalDateTime.now(),LocalDateTime.now(),"admin","admin",null,null,amount,account.getCurrency(),type,LocalDateTime.now(),account));
            return account;
        }
/*
        @Override
        public Account withdraw(Long id, double amount) {
            Account account = getAccountOrThrow(id);

            if (account.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient balance");
            }

            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);

            transactionRepository.save(new Transaction(account, -amount, TransactionType.WITHDRAWAL, LocalDateTime.now()));
            return account;
        }

        @Transactional
        @Override
        public void transfer(Long fromAccountId, Long toAccountId, double amount) {
            Account fromAccount = getAccountOrThrow(fromAccountId);
            Account toAccount = getAccountOrThrow(toAccountId);

            if (fromAccount.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient balance for transfer");
            }

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transactionRepository.save(new Transaction(fromAccount, -amount, TransactionType.TRANSFER, LocalDateTime.now()));
            transactionRepository.save(new Transaction(toAccount, amount, TransactionType.TRANSFER, LocalDateTime.now()));
        }
*/
        @Override
        public List<Transactions> getAccountTransactions(Long id) {
            Account account = getAccountOrThrow(id);
            return transactionsRepository.findByAccount(account);
        }

        private Account getAccountOrThrow(Long id) {
            return accountRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
        }
    }


