package com.example.accountapp.account.service;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Category;
import com.example.accountapp.account.model.Transactions;
import com.example.accountapp.account.reporsitory.AccountRepository;
import com.example.accountapp.account.reporsitory.CategoryRepository;
import com.example.accountapp.account.reporsitory.TransactionsRepository;
import com.example.accountapp.common.Currency;
import com.example.accountapp.common.Status;
import com.example.accountapp.security.model.User;
import com.example.accountapp.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceImplTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionsServiceImpl transactionsService;

    private Account testAccount;
    private User testUser;
    private Category testCategory;
    private Transactions testTransaction;

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

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Food");
        testCategory.setDescription("Food expenses");

        testTransaction = new Transactions();
        testTransaction.setId(1L);
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setType("DEPOSIT");
        testTransaction.setAccount(testAccount);
        testTransaction.setCategory(testCategory);
        testTransaction.setDate(LocalDateTime.now());
    }

    @Test
    void deposit_WhenValidData_ShouldIncreaseBalance() {
        // Given
        BigDecimal depositAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("1500.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionsRepository.save(any(Transactions.class))).thenReturn(testTransaction);

        // When
        Account result = transactionsService.deposit(1L, depositAmount, "DEPOSIT", 1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getBalance());
        verify(accountRepository, times(1)).save(testAccount);
        verify(transactionsRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    void deposit_WhenAccountNotFound_ShouldThrowException() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> transactionsService.deposit(1L, new BigDecimal("500.00"), "DEPOSIT", 1L, 1L));

        assertEquals("Account not found with id: 1", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionsRepository, never()).save(any(Transactions.class));
    }

    @Test
    void withdraw_WhenSufficientBalance_ShouldDecreaseBalance() {
        // Given
        BigDecimal withdrawAmount = new BigDecimal("300.00");
        BigDecimal expectedBalance = new BigDecimal("700.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionsRepository.save(any(Transactions.class))).thenReturn(testTransaction);

        // When
        Account result = transactionsService.withdraw(1L, withdrawAmount, "WITHDRAWAL", 1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(expectedBalance, result.getBalance());
        verify(accountRepository, times(1)).save(testAccount);
        verify(transactionsRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    void withdraw_WhenInsufficientBalance_ShouldThrowException() {
        // Given
        BigDecimal withdrawAmount = new BigDecimal("1500.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> transactionsService.withdraw(1L, withdrawAmount, "WITHDRAWAL", 1L, 1L));

        assertEquals("Insufficient balance", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionsRepository, never()).save(any(Transactions.class));
    }

    @Test
    void transfer_WhenValidData_ShouldTransferMoney() {
        // Given
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(new BigDecimal("500.00"));
        toAccount.setCurrency(Currency.dinar_TN);

        BigDecimal transferAmount = new BigDecimal("200.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        transactionsService.transfer(1L, 2L, transferAmount, 1L);

        // Then
        assertEquals(new BigDecimal("800.00"), testAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionsRepository, times(2)).save(any(Transactions.class));
    }

    @Test
    void transfer_WhenInsufficientBalance_ShouldThrowException() {
        // Given
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(new BigDecimal("500.00"));

        BigDecimal transferAmount = new BigDecimal("1500.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> transactionsService.transfer(1L, 2L, transferAmount, 1L));

        assertEquals("Insufficient balance for transfer", exception.getMessage());
    }

    @Test
    void getAccountTransactions_ShouldReturnTransactions() {
        // Given
        List<Transactions> expectedTransactions = Arrays.asList(testTransaction);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionsRepository.findByAccount(testAccount)).thenReturn(expectedTransactions);

        // When
        List<Transactions> result = transactionsService.getAccountTransactions(1L);

        // Then
        assertEquals(expectedTransactions, result);
        verify(transactionsRepository, times(1)).findByAccount(testAccount);
    }

    @Test
    void getAccountTransactions_WhenAccountNotFound_ShouldThrowException() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> transactionsService.getAccountTransactions(1L));

        assertEquals("Account not found with id: 1", exception.getMessage());
        verify(transactionsRepository, never()).findByAccount(any(Account.class));
    }
}