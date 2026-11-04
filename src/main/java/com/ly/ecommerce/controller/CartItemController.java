package com.ly.ecommerce.controller;

import com.ly.ecommerce.dto.cartItemDto.CartItemRequest;
import com.ly.ecommerce.dto.cartItemDto.CartResponse;
import com.ly.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartItemController {

    private final CartItemService  cartItemService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest cartItemRequest
    ){
        return ResponseEntity.ok(cartItemService.addToCart(userId,cartItemRequest));
    }

    @PostMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId
    ){
        return  ResponseEntity.ok(cartItemService.removeFromCart(userId,productId));
    }
}
