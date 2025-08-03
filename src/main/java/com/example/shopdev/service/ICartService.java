package com.example.shopdev.service;

import com.example.shopdev.dto.req.CartItemRequest;
import com.example.shopdev.dto.req.QuantityRequest;
import com.example.shopdev.dto.res.CartItemResponse;

import java.util.List;

public interface ICartService {
    List<CartItemResponse> getCart();
    CartItemResponse addToCart(CartItemRequest cartItemRequest);
    CartItemResponse updateCartItem(Long cartItemId, QuantityRequest request);
    void deleteCartItem(Long cartItemId);
    void clearCart();
    void checkout();
}
