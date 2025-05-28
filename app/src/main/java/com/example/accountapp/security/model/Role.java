package com.example.accountapp.security.model;

import com.example.accountapp.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {



    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy, String name, Set<User> users) {
        super(createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.name = name;
        this.users = users;
    }

    public Role(LocalDateTime createdDate, LocalDateTime lastModifiedDate, String createdBy, String lastModifiedBy,String name) {
        super(createdDate, lastModifiedDate, createdBy, lastModifiedBy);
        this.name = name;
    }
    public Role() {
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}
