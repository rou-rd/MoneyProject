package com.example.accountapp.account.dto;

import java.math.BigDecimal;

public class TransacationRequest {
    private Long id;
    private BigDecimal amount;
    private String type;

    public TransacationRequest(Long id, BigDecimal amount, String type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}