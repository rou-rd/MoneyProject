package com.example.accountapp.user.model;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.common.Status;
import com.example.accountapp.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class AppUser extends BaseEntity {
    private String firstName;
    private String lastName	;
    private String email;
    private String password;
    private String phoneNumber;
    private Status status;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Account> accounts;

    public AppUser() {

    }

    public AppUser(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, String firstName, String lastName, String email, String password, String phoneNumber, Status status, List<Account> accounts) {
        super(id, createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.accounts = accounts;
    }




    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser user)) return false;
        return ((AppUser) o).getId().equals(user.getId());
    }

}
