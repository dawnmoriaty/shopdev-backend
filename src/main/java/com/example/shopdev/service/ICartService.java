package com.example.shopdev.service;

import com.example.shopdev.dto.req.CartItemRequest;
import com.example.shopdev.dto.req.QuantityRequest;
import com.example.shopdev.dto.res.CartItemResponse;

import java.util.List;

public interface ICartService {
    List<CartItemResponse> getCartByUserId(Long userId);
    CartItemResponse addToCart(Long userId, CartItemRequest cartItemRequest);
    CartItemResponse updateCartItem(Long userId, Long cartItemId, QuantityRequest request);
    void deleteCartItem(Long userId, Long cartItemId);
    void clearCart(Long userId);
    void checkout(Long userId);
}
