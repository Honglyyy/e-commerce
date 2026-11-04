package com.ly.ecommerce.dto.cartItemDto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse (
        Long productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
){
}
