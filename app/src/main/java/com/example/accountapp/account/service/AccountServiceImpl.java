package com.example.accountapp.account.service;

import com.example.accountapp.account.reporsitory.AccountRepository;
import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.reporsitory.TransactionsRepository;
import com.example.accountapp.common.Currency;
import com.example.accountapp.common.Status;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;



    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, TransactionsRepository transactionsRepository) {
        this.userRepository=userRepository;
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {

        return accountRepository.findAll();
    }
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
    public Account createAccount(AccountRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setCreatedDate(LocalDateTime.now());
        account.setLastModifiedDate(LocalDateTime.now());
        account.setBalance(dto.getBalance());
        account.setStatus(Status.Pending);
        account.setCurrency(Currency.dinar_TN);
        account.setOwner(user);
        account.setCreatedBy(user.getUsername());

        return accountRepository.save(account);
    }
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
    public Account updateAccount(Long id, Account updatedAccount) {
        return accountRepository.findById(id).map(account -> {
            account.setBalance(updatedAccount.getBalance());
            account.setCurrency(updatedAccount.getCurrency());
            account.setStatus(updatedAccount.getStatus());
            account.setLastModifiedDate(LocalDateTime.now());
            return accountRepository.save(account);
        }).orElseThrow(() -> new RuntimeException("Compte non trouvé"));
    }
}
