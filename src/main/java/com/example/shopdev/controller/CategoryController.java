package com.example.shopdev.controller;

import com.example.shopdev.dto.req.CategoryRequest;
import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.dto.res.CategoryResponse;
import com.example.shopdev.model.Category;
import com.example.shopdev.service.impl.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<Category>>builder()
                        .success(true)
                        .status(200)
                        .message("Lấy tất cả danh mục thành công")
                        .data(categories)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category response = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<Category>builder()
                        .success(true)
                        .status(201)
                        .message("Thêm danh mục thành công")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.<Category>builder()
                        .success(true)
                        .status(200)
                        .message("Lấy danh mục thành công với id "+id)
                        .data(category)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        Category updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(
                ApiResponse.<Category>builder()
                        .success(true)
                        .status(200)
                        .message("Cập nhật danh mục thành công với id " + id)
                        .data(updatedCategory)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .status(200)
                        .message("Xoá danh mục thành công với id " + id)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategoryStatus(
            @PathVariable Long id,
            @RequestBody CategoryRequest status) {
        CategoryResponse updatedCategory = categoryService.updateStatus(id, status.getStatus());
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .status(200)
                        .message("Cập nhật trạng thái danh mục thành công với id " + id)
                        .data(updatedCategory)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
