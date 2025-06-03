package com.example.accountapp.account.service;

import com.example.accountapp.account.dto.CategoryDto;
import com.example.accountapp.account.model.Account;
import com.example.accountapp.account.model.Category;

public interface CategoryService {
    Category createCategory(CategoryDto dto);
    void deleteCategory(Long id);
    Account updateCategory(Long id, Category updatedCategory);
}
