package com.example.accountapp.account.dto;

import java.math.BigDecimal;

public class TransacationRequest {
    private Long idUser;
    private Long idAccount;
    private BigDecimal amount;

    private Long toAccountId;

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }



    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    private Long fromAccountId;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    private String type;

    public TransacationRequest(Long idUser, Long idAccount, BigDecimal amount, Long toAccountId, Long fromAccountId, String type) {
        this.idUser = idUser;
        this.idAccount = idAccount;
        this.amount = amount;
        this.toAccountId = toAccountId;
        this.fromAccountId = fromAccountId;
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