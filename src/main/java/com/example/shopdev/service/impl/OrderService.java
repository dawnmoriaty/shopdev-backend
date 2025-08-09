package com.example.shopdev.service.impl;

import com.example.shopdev.constants.OrderStatus;
import com.example.shopdev.dto.req.CreateOrderRequest;
import com.example.shopdev.dto.res.OrderItemResponse;
import com.example.shopdev.dto.res.OrderResponse;
import com.example.shopdev.exception.ForbiddenException;
import com.example.shopdev.exception.ResourceNotFoundException;
import com.example.shopdev.model.*;
import com.example.shopdev.repository.*;
import com.example.shopdev.security.jwt.JwtCache;
import com.example.shopdev.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final IProductRepository productRepository;
    private final IAddressRepository addressRepository;
    private final JwtCache jwtCache;

    @Override
    @Transactional
    public OrderResponse checkout(CreateOrderRequest request) {
        Long userId = jwtCache.getCurrentUserId();

        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (!address.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not own this address");
        }

        Order order = new Order();
        order.setSerialNumber(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNote(request.getNote());
        order.setShippingName(address.getReceiveName());
        order.setShippingPhone(address.getPhone());
        order.setShippingAddress(address.getAddress());

        BigDecimal total = BigDecimal.ZERO;

        for (Cart c : cartItems) {
            Product product = productRepository.findById(c.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + c.getProductId()));

            BigDecimal unitPrice = product.getUnitPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(c.getQuantity()));

            OrderItem item = OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .unitPrice(unitPrice)
                    .quantity(c.getQuantity())
                    .lineTotal(lineTotal)
                    .build();

            order.addItem(item);
            total = total.add(lineTotal);
        }

        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        // Xóa giỏ hàng sau khi tạo đơn
        cartRepository.deleteByUserId(userId);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getCurrentUserOrders() {
        Long userId = jwtCache.getCurrentUserId();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getCurrentUserOrderBySerial(String serialNumber) {
        Long userId = jwtCache.getCurrentUserId();
        Order order = orderRepository.findBySerialNumberAndUserId(serialNumber, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getCurrentUserOrdersByStatus(OrderStatus status) {
        Long userId = jwtCache.getCurrentUserId();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(o -> o.getStatus() == status)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelCurrentUserOrder(Long orderId) {
        Long userId = jwtCache.getCurrentUserId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new ForbiddenException("You do not own this order");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be canceled");
        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    // ADMIN
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Có thể thêm kiểm tra chuyển trạng thái hợp lệ nếu cần
        order.setStatus(newStatus);
        return mapToResponse(orderRepository.save(order));
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());
        res.setSerialNumber(order.getSerialNumber());
        res.setUserId(order.getUserId());
        res.setStatus(order.getStatus());
        res.setPaymentMethod(order.getPaymentMethod());
        res.setTotalAmount(order.getTotalAmount());
        res.setShippingName(order.getShippingName());
        res.setShippingPhone(order.getShippingPhone());
        res.setShippingAddress(order.getShippingAddress());
        res.setNote(order.getNote());
        res.setCreatedAt(order.getCreatedAt());
        res.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemResponse> items = order.getItems().stream().map(oi -> {
            OrderItemResponse i = new OrderItemResponse();
            i.setId(oi.getId());
            i.setProductId(oi.getProductId());
            i.setProductName(oi.getProductName());
            i.setUnitPrice(oi.getUnitPrice());
            i.setQuantity(oi.getQuantity());
            i.setLineTotal(oi.getLineTotal());
            return i;
        }).toList();
        res.setItems(items);
        return res;
    }
}