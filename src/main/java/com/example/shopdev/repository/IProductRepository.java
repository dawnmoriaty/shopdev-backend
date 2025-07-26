package com.example.shopdev.repository;

import com.example.shopdev.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product,Long> {
    List<Product> findProductByCategoryId(Long categoryId);
}
