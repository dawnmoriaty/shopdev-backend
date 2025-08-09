package com.example.shopdev.dto.res;

import com.example.shopdev.constants.OrderStatus;
import com.example.shopdev.constants.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String serialNumber;
    private Long userId;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;

    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;

    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItemResponse> items;
}