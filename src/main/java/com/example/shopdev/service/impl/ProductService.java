package com.example.shopdev.service.impl;

import com.example.shopdev.dto.req.ProductRequest;
import com.example.shopdev.dto.res.ProductResponse;
import com.example.shopdev.model.Category;
import com.example.shopdev.model.Product;
import com.example.shopdev.repository.ICategoryRepository;
import com.example.shopdev.repository.IProductRepository;
import com.example.shopdev.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Không tìm thấy Product với id: ";
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Không tìm thấy Category với id: ";

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = findCategoryById(request.getCategoryId());

        Product product = new Product();
        product.setSku(UUID.randomUUID());
        updateProductFromRequest(product, request, category);

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product existingProduct = findProductById(productId);
        Category category = findCategoryById(request.getCategoryId());

        updateProductFromRequest(existingProduct, request, category);
        Product updatedProduct = productRepository.save(existingProduct);
        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return mapToResponse(findProductById(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + categoryId);
        }
        List<Product> products = productRepository.findProductByCategoryId(categoryId);
        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + productId));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + categoryId));
    }

    private void updateProductFromRequest(Product product, ProductRequest request, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setUnitPrice(request.getUnitPrice());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setUnitPrice(product.getUnitPrice());
        response.setImageUrl(product.getImageUrl());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }
}