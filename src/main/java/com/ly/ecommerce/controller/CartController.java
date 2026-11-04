package com.ly.ecommerce.controller;

import com.ly.ecommerce.dto.cartItemDto.CartItemRequest;
import com.ly.ecommerce.dto.cartItemDto.CartResponse;
import com.ly.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest cartItemRequest
    ){
        return ResponseEntity.ok(cartService.addToCart(userId,cartItemRequest));
    }

    @PostMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId
    ){
        return  ResponseEntity.ok(cartService.removeFromCart(userId,productId));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader("X-User-ID") String userId
    ){
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }
}
