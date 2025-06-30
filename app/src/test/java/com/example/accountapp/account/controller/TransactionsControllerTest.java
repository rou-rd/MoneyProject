package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.TransacationRequest;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Transactions;
import com.example.accountapp.account.service.TransactionsServiceImpl;
import com.example.accountapp.common.Currency;
import com.example.accountapp.common.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionsController.class)
class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionsServiceImpl transactionsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;
    private Transactions testTransaction;
    private TransacationRequest transactionRequest;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setStatus(Status.INFORCE);
        testAccount.setCurrency(Currency.dinar_TN);

        testTransaction = new Transactions();
        testTransaction.setId(1L);
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setType("DEPOSIT");
        testTransaction.setDate(LocalDateTime.now());

        transactionRequest = new TransacationRequest(
            1L, // idUser
            1L, // idAccount
            new BigDecimal("100.00"), // amount
            1L, // categoryId
            null, // toAccountId
            null, // fromAccountId
            "DEPOSIT" // type
        );
    }

    @Test
    void deposit_WhenValidData_ShouldReturnSuccess() throws Exception {
        // Given
        when(transactionsService.deposit(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong()))
                .thenReturn(testAccount);

        // When & Then
        mockMvc.perform(post("/Transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deposit completed successfully"));

        verify(transactionsService, times(1)).deposit(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong());
    }

    @Test
    void deposit_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Given
        when(transactionsService.deposit(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong()))
                .thenThrow(new RuntimeException("Deposit failed"));

        // When & Then
        mockMvc.perform(post("/Transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Deposit failed"));

        verify(transactionsService, times(1)).deposit(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong());
    }

    @Test
    void withdraw_WhenValidData_ShouldReturnSuccess() throws Exception {
        // Given
        when(transactionsService.withdraw(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong()))
                .thenReturn(testAccount);

        // When & Then
        mockMvc.perform(post("/Transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Withdrawal completed successfully"));

        verify(transactionsService, times(1)).withdraw(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong());
    }

    @Test
    void withdraw_WhenInsufficientBalance_ShouldReturnBadRequest() throws Exception {
        // Given
        when(transactionsService.withdraw(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong()))
                .thenThrow(new IllegalArgumentException("Insufficient balance"));

        // When & Then
        mockMvc.perform(post("/Transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Insufficient balance"));

        verify(transactionsService, times(1)).withdraw(anyLong(), any(BigDecimal.class), anyString(), anyLong(), anyLong());
    }

    @Test
    void transfer_WhenValidData_ShouldReturnSuccess() throws Exception {
        // Given
        TransacationRequest transferRequest = new TransacationRequest(
            1L, null, new BigDecimal("100.00"), null, 2L, 1L, "TRANSFER"
        );
        doNothing().when(transactionsService).transfer(anyLong(), anyLong(), any(BigDecimal.class), anyLong());

        // When & Then
        mockMvc.perform(post("/Transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transfer completed successfully"));

        verify(transactionsService, times(1)).transfer(anyLong(), anyLong(), any(BigDecimal.class), anyLong());
    }

    @Test
    void getAccountTransactions_WhenTransactionsExist_ShouldReturnTransactions() throws Exception {
        // Given
        List<Transactions> transactions = Arrays.asList(testTransaction);
        when(transactionsService.getAccountTransactions(1L)).thenReturn(transactions);

        // When & Then
        mockMvc.perform(get("/Transactions/transactionsbyaccount/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1));

        verify(transactionsService, times(1)).getAccountTransactions(1L);
    }

    @Test
    void getAccountTransactions_WhenNoTransactions_ShouldReturnEmptyList() throws Exception {
        // Given
        when(transactionsService.getAccountTransactions(1L)).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/Transactions/transactionsbyaccount/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("No transactions found for this account"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(transactionsService, times(1)).getAccountTransactions(1L);
    }
}