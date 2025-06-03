package com.example.accountapp.account.service;

import com.example.accountapp.account.dto.CategoryDto;
import com.example.accountapp.account.model.Category;
import com.example.accountapp.account.reporsitory.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl {
    private CategoryRepository categoryRepository;

    public Category createCategory(CategoryDto dto){
        Category cat=new Category();
        return categoryRepository.save(cat);
    }
}
