package com.example.shopdev.controller;

import com.example.shopdev.constants.OrderStatus;
import com.example.shopdev.dto.req.UpdateOrderStatusRequest;
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
@RequestMapping("/api/v1/admin/orders")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminOrderController {

    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll() {
        List<OrderResponse> data = orderService.getAllOrders();
        ApiResponse<List<OrderResponse>> res = ApiResponse.<List<OrderResponse>>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Lấy danh sách đơn hàng thành công")
                .data(data).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/status/{orderStatus}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByStatus(@PathVariable OrderStatus orderStatus) {
        List<OrderResponse> data = orderService.getOrdersByStatus(orderStatus);
        ApiResponse<List<OrderResponse>> res = ApiResponse.<List<OrderResponse>>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Lấy danh sách đơn hàng theo trạng thái thành công")
                .data(data).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long orderId) {
        OrderResponse data = orderService.getOrderById(orderId);
        ApiResponse<OrderResponse> res = ApiResponse.<OrderResponse>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Lấy chi tiết đơn hàng thành công")
                .data(data).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest req) {
        OrderResponse data = orderService.updateOrderStatus(orderId, req.getOrderStatus());
        ApiResponse<OrderResponse> res = ApiResponse.<OrderResponse>builder()
                .success(true).status(HttpStatus.OK.value())
                .message("Cập nhật trạng thái đơn hàng thành công")
                .data(data).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.ok(res);
    }
}