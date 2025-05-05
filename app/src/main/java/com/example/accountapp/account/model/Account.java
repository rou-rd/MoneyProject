package com.example.accountapp.account.model;

import com.example.accountapp.common.BaseEntity;
import com.example.accountapp.common.Currency;
import com.example.accountapp.common.Status;
import com.example.accountapp.user.model.AppUser;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
public class Account extends BaseEntity {

    private BigDecimal balance ;
    private Status status;
    private Currency currency;
    @ManyToOne()
    @JoinColumn(name = "app_user_id")
    private AppUser user;


    public Account() {

    }

    public Account(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, AppUser owner, BigDecimal balance, Status status, Currency currency) {
        super(id, createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.user = owner;
        this.balance = balance;
        this.status = status;
        this.currency = currency;
    }


    public AppUser getOwner() {
        return user;
    }

    public void setOwner(AppUser owner) {
        this.user = owner;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return user.getId().equals(account.user.getId());
    }

}
