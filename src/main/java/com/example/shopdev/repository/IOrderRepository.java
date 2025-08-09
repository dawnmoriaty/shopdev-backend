package com.example.shopdev.repository;

import com.example.shopdev.constants.OrderStatus;
import com.example.shopdev.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findBySerialNumberAndUserId(String serialNumber, Long userId);
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

}
