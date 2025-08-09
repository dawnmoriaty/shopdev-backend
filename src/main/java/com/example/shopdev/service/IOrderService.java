package com.example.shopdev.service;

import com.example.shopdev.constants.OrderStatus;
import com.example.shopdev.dto.req.CreateOrderRequest;
import com.example.shopdev.dto.res.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse checkout(CreateOrderRequest request);

    // USER
    List<OrderResponse> getCurrentUserOrders();
    OrderResponse getCurrentUserOrderBySerial(String serialNumber);
    List<OrderResponse> getCurrentUserOrdersByStatus(OrderStatus status);
    void cancelCurrentUserOrder(Long orderId);

    // ADMIN
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByStatus(OrderStatus status);
    OrderResponse getOrderById(Long orderId);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
}