package com.example.shopdev.service;

import com.example.shopdev.dto.req.CategoryRequest;
import com.example.shopdev.dto.res.CategoryResponse;
import com.example.shopdev.model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryRequest request);

    Category updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();
    CategoryResponse updateStatus(Long id, Boolean status) ;
}
