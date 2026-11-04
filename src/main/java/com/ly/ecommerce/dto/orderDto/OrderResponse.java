package com.ly.ecommerce.dto.orderDto;

import com.ly.ecommerce.model.OrderItem;
import com.ly.ecommerce.model.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
        Long id,
        BigDecimal total,
        OrderStatus status,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
){
}
