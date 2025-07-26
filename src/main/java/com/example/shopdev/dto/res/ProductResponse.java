package com.example.shopdev.dto.res;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class ProductResponse {
    private Long id;
    private UUID sku;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;
    private String categoryName;
}
