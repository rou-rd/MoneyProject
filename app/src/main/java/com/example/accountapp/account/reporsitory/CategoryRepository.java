package com.example.accountapp.account.reporsitory;

import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long categoryId);
}
