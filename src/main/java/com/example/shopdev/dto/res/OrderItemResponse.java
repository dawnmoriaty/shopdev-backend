package com.example.shopdev.dto.res;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}
