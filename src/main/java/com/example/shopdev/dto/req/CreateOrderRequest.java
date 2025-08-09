package com.example.shopdev.dto.req;

import com.example.shopdev.constants.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateOrderRequest {
    @NotNull(message = "addressId không được để trống")
    private Long addressId;

    @NotNull(message = "paymentMethod không được để trống")
    private PaymentMethod paymentMethod;

    private String note;
}
