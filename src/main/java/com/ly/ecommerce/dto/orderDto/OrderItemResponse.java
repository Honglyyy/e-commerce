package com.ly.ecommerce.dto.orderDto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
