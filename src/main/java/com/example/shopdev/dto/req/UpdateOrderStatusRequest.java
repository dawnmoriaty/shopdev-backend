package com.example.shopdev.dto.req;

import com.example.shopdev.constants.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull(message = "orderStatus không được để trống")
    private OrderStatus orderStatus;
}