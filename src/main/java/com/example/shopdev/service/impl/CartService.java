package com.example.shopdev.service.impl;

import com.example.shopdev.dto.req.CartItemRequest;
import com.example.shopdev.dto.req.QuantityRequest;
import com.example.shopdev.dto.res.CartItemResponse;
import com.example.shopdev.dto.res.ProductResponse;
import com.example.shopdev.model.Cart;
import com.example.shopdev.repository.ICartRepository;
import com.example.shopdev.security.jwt.JwtCache;
import com.example.shopdev.service.ICartService;
import com.example.shopdev.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final ICartRepository cartRepository;
    private final IProductService productService;
    private final JwtCache jwtCache;

    @Override
    @Transactional
    public List<CartItemResponse> getCart() {
        Long userId = jwtCache.getCurrentUserId();
        List<Cart> cartList = cartRepository.findByUserId(userId);
        return cartList.stream()
                .map(this::convertToCartItemResponse)
                .toList();
    }

    @Override
    @Transactional
    public CartItemResponse addToCart(CartItemRequest cartItemRequest) {
        Long userId = jwtCache.getCurrentUserId();

        List<Cart> existItems = cartRepository.findByUserId(userId);
        for(Cart item : existItems) {
            if(item.getProductId().equals(cartItemRequest.getProductId())) {
                item.setQuantity(item.getQuantity() + cartItemRequest.getQuantity());
                cartRepository.save(item);
                return convertToCartItemResponse(item);
            }
        }

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(cartItemRequest.getProductId());
        cart.setQuantity(cartItemRequest.getQuantity());
        return convertToCartItemResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(Long cartItemId, QuantityRequest request) {
        Long userId = jwtCache.getCurrentUserId();

        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }
        cart.setQuantity(request.getQuantity());
        return convertToCartItemResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        Long userId = jwtCache.getCurrentUserId();

        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }
        cartRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void clearCart() {
        Long userId = jwtCache.getCurrentUserId();
        cartRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void checkout() {
        Long userId = jwtCache.getCurrentUserId();

        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        cartRepository.deleteByUserId(userId);
    }

    private CartItemResponse convertToCartItemResponse(Cart cart) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cart.getId());
        response.setProductId(cart.getProductId());
        response.setQuantity(cart.getQuantity());

        ProductResponse product = productService.getProductById(cart.getProductId());
        try {
            response.setProductName(product.getName());
            response.setProductPrice(product.getUnitPrice());
            response.setProductImage(product.getImageUrl());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving product details for cart item: " + cart.getId(), e);
        }
        return response;
    }
}
