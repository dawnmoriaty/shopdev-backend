package com.example.shopdev.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải là số dương")
    private BigDecimal unitPrice;

    private String imageUrl ;

    @NotNull(message = "ID của Category không được để trống")
    private Long categoryId;
}
