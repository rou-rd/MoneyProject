package com.example.accountapp.account.service;

import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.reporsitory.AccountRepository;
import com.example.accountapp.common.Currency;
import com.example.accountapp.common.Status;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User testUser;
    private Account testAccount;
    private AccountRequestDTO testAccountRequestDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setStatus(Status.INFORCE);
        testAccount.setCurrency(Currency.dinar_TN);
        testAccount.setOwner(testUser);
        testAccount.setCreatedDate(LocalDateTime.now());

        testAccountRequestDTO = new AccountRequestDTO();
        testAccountRequestDTO.setBalance(new BigDecimal("500.00"));
        testAccountRequestDTO.setStatus(Status.Pending);
        testAccountRequestDTO.setCurrency(Currency.dinar_TN);
        testAccountRequestDTO.setUserId(1L);
    }

    @Test
    void getAllAccounts_ShouldReturnAllAccounts() {
        // Given
        List<Account> expectedAccounts = Arrays.asList(testAccount);
        when(accountRepository.findAll()).thenReturn(expectedAccounts);

        // When
        List<Account> actualAccounts = accountService.getAllAccounts();

        // Then
        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void getAccountById_WhenAccountExists_ShouldReturnAccount() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // When
        Optional<Account> result = accountService.getAccountById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testAccount, result.get());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void getAccountById_WhenAccountDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Account> result = accountService.getAccountById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void createAccount_WhenUserExists_ShouldCreateAccount() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.createAccount(testAccountRequestDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> accountService.createAccount(testAccountRequestDTO));
        
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void deleteAccount_ShouldCallRepositoryDelete() {
        // When
        accountService.deleteAccount(1L);

        // Then
        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateAccount_WhenAccountExists_ShouldUpdateAccount() {
        // Given
        Account updatedAccount = new Account();
        updatedAccount.setBalance(new BigDecimal("2000.00"));
        updatedAccount.setCurrency(Currency.Euro);
        updatedAccount.setStatus(Status.INFORCE);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        Account result = accountService.updateAccount(1L, updatedAccount);

        // Then
        assertNotNull(result);
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateAccount_WhenAccountDoesNotExist_ShouldThrowException() {
        // Given
        Account updatedAccount = new Account();
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> accountService.updateAccount(1L, updatedAccount));
        
        assertEquals("Compte non trouvé", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }
}