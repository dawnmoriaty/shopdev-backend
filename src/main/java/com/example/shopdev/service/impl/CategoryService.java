package com.example.shopdev.service.impl;

import com.example.shopdev.dto.req.CategoryRequest;
import com.example.shopdev.dto.res.CategoryResponse;
import com.example.shopdev.model.Category;
import com.example.shopdev.repository.ICategoryRepository;
import com.example.shopdev.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest request) {
        Category createCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : true)
                .build();
        return categoryRepository.save(createCategory);
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest request) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(request.getName());
        existingCategory.setDescription(request.getDescription());
        existingCategory.setStatus(request.getStatus() != null ? request.getStatus() : existingCategory.getStatus());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryResponse updateStatus(Long id, Boolean status) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setStatus(status);
        Category updatedStatus = categoryRepository.save(existingCategory);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(updatedStatus.getId());
        categoryResponse.setStatus(updatedStatus.getStatus());
        return categoryResponse;
    }
}
