package com.example.accountapp.account.dto;

import com.example.accountapp.common.Currency;
import com.example.accountapp.common.BaseEntity;
import com.example.accountapp.common.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountRequestDTO extends BaseEntity {

        private BigDecimal balance;
        private Status status;
        private Currency currency;
        private Long userId;

    public AccountRequestDTO(BigDecimal balance, Status status, Currency currency, Long userId) {
        this.balance = balance;
        this.status = status;
        this.currency = currency;
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AccountRequestDTO(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, BigDecimal balance, Status status, Currency currency, Long userId) {
        super(id, createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.balance = balance;
        this.status = status;
        this.currency = currency;
        this.userId = userId;
    }
    public AccountRequestDTO() {

    }
}
