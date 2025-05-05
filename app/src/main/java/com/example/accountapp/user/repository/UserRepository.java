package com.example.accountapp.user.repository;

import com.example.accountapp.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findById(Long id);

    List<AppUser> findAll();
}
