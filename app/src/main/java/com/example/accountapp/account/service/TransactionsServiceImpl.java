package com.example.accountapp.account.service;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Category;
import com.example.accountapp.account.model.Transactions;
import com.example.accountapp.account.reporsitory.AccountRepository;
import com.example.accountapp.account.reporsitory.CategoryRepository;
import com.example.accountapp.account.reporsitory.TransactionsRepository;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;


    public TransactionsServiceImpl(TransactionsRepository transactionsRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.transactionsRepository = transactionsRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
        public Account deposit(Long id, BigDecimal amount,String type, Long idUser,Long categoryId) {
            Account account = getAccountOrThrow(id);
            Category category=getCategoryOrThrow(categoryId);
            User user = getUserOrThrow(idUser);
            account.setLastModifiedBy(user.getEmail());
            account.setLastModifiedDate(LocalDateTime.now());
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);

            transactionsRepository.save(new Transactions(LocalDateTime.now(),LocalDateTime.now(),user.getEmail(),user.getEmail(),null,null,amount,account.getCurrency(),type,LocalDateTime.now(),account,category));
            return account;
        }
        @Override
        public Account withdraw(Long idAccount, BigDecimal amount, String type, Long idUser,Long categoryId) {
            Account account = getAccountOrThrow(idAccount);
            Category category=getCategoryOrThrow(categoryId);
            User user = getUserOrThrow(idUser);
            if (account.getBalance().compareTo(amount) >1  ) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            account.setLastModifiedBy(user.getEmail());
            account.setLastModifiedDate(LocalDateTime.now());
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);

            transactionsRepository.save(new Transactions(LocalDateTime.now(),LocalDateTime.now(),user.getEmail(),user.getEmail(),null,null,amount,account.getCurrency(),type,LocalDateTime.now(),account,category));
            return account;
        }


        @Transactional
        @Override
        public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount,Long idUser) {
            Account fromAccount = getAccountOrThrow(fromAccountId);
            Account toAccount = getAccountOrThrow(toAccountId);
            User user = getUserOrThrow(idUser);
            if (fromAccount.getBalance().compareTo(amount) >1) {
                throw new IllegalArgumentException("Insufficient balance for transfer");
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transactionsRepository.save(new Transactions(LocalDateTime.now(),LocalDateTime.now(),user.getEmail(),user.getEmail(),fromAccountId,toAccountId,amount.negate(),fromAccount.getCurrency(),"transferOut",LocalDateTime.now(),fromAccount));
            transactionsRepository.save(new Transactions(LocalDateTime.now(),LocalDateTime.now(),user.getEmail(),user.getEmail(),fromAccountId,toAccountId,amount,fromAccount.getCurrency(),"transferIn",LocalDateTime.now(),toAccount));
        }

        @Override
        public List<Transactions> getAccountTransactions(Long id) {
            Account account = getAccountOrThrow(id);
            return transactionsRepository.findByAccount(account);
        }


        private Account getAccountOrThrow(Long id) {
            return accountRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
        }
        private User getUserOrThrow(Long id){
            return userRepository.findById(id).orElseThrow(()->  new EntityNotFoundException("User not found with id: " + id));
        }
        private Category getCategoryOrThrow(Long id){
            return categoryRepository.findById(id).orElseThrow(()->  new EntityNotFoundException("Category not found with id: " + id));
        }
    }


