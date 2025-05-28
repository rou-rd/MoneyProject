package com.example.accountapp.account.model;

import com.example.accountapp.common.BaseEntity;
import com.example.accountapp.common.Currency;
import com.example.accountapp.common.Status;
import com.example.accountapp.security.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
public class Account extends BaseEntity {

    private BigDecimal balance ;
    private Status status;
    private Currency currency;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Transactions> transactions;

    public Account() {

    }

    public Account(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, BigDecimal balance, Status status, Currency currency, User user, List<Transactions> transactions) {
        super(createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.balance = balance;
        this.status = status;
        this.currency = currency;
        this.user = user;
        this.transactions = transactions;
    }

    public User getOwner() {
        return user;
    }

    public void setOwner(User owner) {
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
