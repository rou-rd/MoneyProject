package com.example.accountapp.account.controller;

import com.example.accountapp.account.dto.AccountRequestDTO;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.service.AccountServiceImpl;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account testAccount;
    private AccountRequestDTO accountRequestDTO;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setStatus(Status.INFORCE);
        testAccount.setCurrency(Currency.dinar_TN);
        testAccount.setCreatedDate(LocalDateTime.now());

        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setBalance(new BigDecimal("500.00"));
        accountRequestDTO.setStatus(Status.Pending);
        accountRequestDTO.setCurrency(Currency.dinar_TN);
        accountRequestDTO.setUserId(1L);
    }

    @Test
    void getAllAccounts_ShouldReturnAccountsList() throws Exception {
        // Given
        List<Account> accounts = Arrays.asList(testAccount);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        // When & Then
        mockMvc.perform(get("/Account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Accounts retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    void getAccountById_WhenAccountExists_ShouldReturnAccount() throws Exception {
        // Given
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(testAccount));

        // When & Then
        mockMvc.perform(get("/Account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.message").value("Account found"))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    void getAccountById_WhenAccountNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        when(accountService.getAccountById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/Account/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Account not found with ID: 1"));

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    void createAccount_WhenValidData_ShouldCreateAccount() throws Exception {
        // Given
        when(accountService.createAccount(any(AccountRequestDTO.class))).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(post("/Account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Account created successfully"))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(accountService, times(1)).createAccount(any(AccountRequestDTO.class));
    }

    @Test
    void deleteAccount_ShouldDeleteAccount() throws Exception {
        // Given
        doNothing().when(accountService).deleteAccount(1L);

        // When & Then
        mockMvc.perform(delete("/Account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Account deleted successfully"));

        verify(accountService, times(1)).deleteAccount(1L);
    }

    @Test
    void updateAccount_WhenAccountExists_ShouldUpdateAccount() throws Exception {
        // Given
        when(accountService.updateAccount(anyLong(), any(Account.class))).thenReturn(testAccount);

        // When & Then
        mockMvc.perform(put("/Account/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Account updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(accountService, times(1)).updateAccount(anyLong(), any(Account.class));
    }

    @Test
    void updateAccount_WhenAccountNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        when(accountService.updateAccount(anyLong(), any(Account.class)))
                .thenThrow(new RuntimeException("Account not found"));

        // When & Then
        mockMvc.perform(put("/Account/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAccount)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Account not found"));

        verify(accountService, times(1)).updateAccount(anyLong(), any(Account.class));
    }
}