package com.example.accountapp.account.dto;

import java.math.BigDecimal;

public class TransacationRequest {
    private Long idUser;
    private Long idAccount;
    private BigDecimal amount;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    private String type;

    public TransacationRequest(Long idUser, Long idAccount, BigDecimal amount, String type) {
        this.idUser = idUser;
        this.idAccount = idAccount;
        this.amount = amount;
        this.type = type;
    }

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
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