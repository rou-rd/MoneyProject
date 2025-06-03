package com.example.accountapp.account.model;

import com.example.accountapp.common.BaseEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;

    private String description;

    // One category can have many transactions
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transactions> transactions;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }
}
