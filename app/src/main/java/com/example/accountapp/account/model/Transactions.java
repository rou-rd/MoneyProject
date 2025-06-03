package com.example.accountapp.account.model;

import com.example.accountapp.common.BaseEntity;
import com.example.accountapp.common.Currency;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
public class Transactions extends BaseEntity {

    public Transactions() {
    }

    public Transactions(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, Long fromAccountId, Long toAccountId, BigDecimal amount, Currency currency, String type, LocalDateTime date, Account account) {
        super(createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.date = date;
        this.account = account;
    }

    public Transactions(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, Long fromAccountId, Long toAccountId, BigDecimal amount, Currency currency, String type, LocalDateTime date, Account account, Category category) {
        super(createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.date = date;
        this.account = account;
        this.category = category;
    }

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private Currency currency;
    private String type; // "DEPOSIT", "WITHDRAWAL", "TRANSFER"

    private LocalDateTime date;

    @ManyToOne()
    @JoinColumn(name = "account")
    private Account account;


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


}
