package com.ly.ecommerce.dto.cartItemDto;

public record CartItemRequest(
        Long productId,
        Integer quantity
) {
}
