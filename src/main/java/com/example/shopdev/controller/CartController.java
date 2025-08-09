package com.example.shopdev.controller;

import com.example.shopdev.dto.req.CartItemRequest;
import com.example.shopdev.dto.req.CreateOrderRequest;
import com.example.shopdev.dto.req.QuantityRequest;
import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.dto.res.CartItemResponse;
import com.example.shopdev.dto.res.OrderResponse;
import com.example.shopdev.service.ICartService;
import com.example.shopdev.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final ICartService cartService;
    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCart() {
        List<CartItemResponse> cartItems = cartService.getCart();

        ApiResponse<List<CartItemResponse>> response = ApiResponse.<List<CartItemResponse>>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Lấy giỏ hàng thành công")
                .data(cartItems)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(@RequestBody CartItemRequest request) {
        CartItemResponse cartItem = cartService.addToCart(request);

        ApiResponse<CartItemResponse> response = ApiResponse.<CartItemResponse>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Thêm sản phẩm vào giỏ hàng thành công")
                .data(cartItem)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody QuantityRequest request) {
        CartItemResponse cartItem = cartService.updateCartItem(cartItemId, request);

        ApiResponse<CartItemResponse> response = ApiResponse.<CartItemResponse>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Cập nhật số lượng thành công")
                .data(cartItem)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartItemId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Xóa sản phẩm khỏi giỏ hàng thành công")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clearCart();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Xóa toàn bộ giỏ hàng thành công")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // Đặt hàng từ giỏ
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(@RequestBody CreateOrderRequest request) {
        OrderResponse data = orderService.checkout(request);

        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Đặt hàng thành công")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}