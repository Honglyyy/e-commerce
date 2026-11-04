package com.ly.ecommerce.dto.cartItemDto;

import lombok.Builder;

import java.util.List;

@Builder
public record CartResponse(
        Long userId,
        List<CartItemResponse>  items
){
}
