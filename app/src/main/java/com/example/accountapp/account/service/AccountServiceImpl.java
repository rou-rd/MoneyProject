package com.example.accountapp.account.service;

import com.example.accountapp.account.reporsitory.AccountRepository;
import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.user.model.AppUser;
import com.example.accountapp.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
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
        AppUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setCreatedDate(dto.getCreatedDate());
        account.setLastModifiedDate(dto.getLastModifiedDate());
        account.setCreatedBy(dto.getCreatedBy());
        account.setBalance(dto.getBalance());
        account.setStatus(dto.getStatus());
        account.setCurrency(dto.getCurrency());
        account.setOwner(user);

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
