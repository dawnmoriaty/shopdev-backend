package com.example.shopdev.controller;

import com.example.shopdev.constants.OrderStatus;
import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.dto.res.OrderResponse;
import com.example.shopdev.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
public class UserOrderController {

    private final IOrderService orderService;

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getHistory() {
        List<OrderResponse> data = orderService.getCurrentUserOrders();
        ApiResponse<List<OrderResponse>> res = ApiResponse.<List<OrderResponse>>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Lấy lịch sử đơn hàng thành công")
                .data(data).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/history/{serialNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getBySerial(@PathVariable String serialNumber) {
        OrderResponse data = orderService.getCurrentUserOrderBySerial(serialNumber);
        ApiResponse<OrderResponse> res = ApiResponse.<OrderResponse>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Lấy chi tiết đơn hàng thành công")
                .data(data).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }

    // Tránh đụng route với serialNumber
    @GetMapping("/history/status/{orderStatus}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByStatus(@PathVariable OrderStatus orderStatus) {
        List<OrderResponse> data = orderService.getCurrentUserOrdersByStatus(orderStatus);
        ApiResponse<List<OrderResponse>> res = ApiResponse.<List<OrderResponse>>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Lấy lịch sử theo trạng thái thành công")
                .data(data).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }

    @PutMapping("/history/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long orderId) {
        orderService.cancelCurrentUserOrder(orderId);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Hủy đơn hàng thành công")
                .timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }
}