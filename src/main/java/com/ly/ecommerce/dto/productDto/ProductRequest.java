package com.ly.ecommerce.dto.productDto;

import java.math.BigDecimal;

public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        String category,
        String imageUrl,
        Boolean active
) {
}
