package com.ly.ecommerce.mapper;

import com.ly.ecommerce.dto.orderDto.OrderItemResponse;
import com.ly.ecommerce.dto.orderDto.OrderResponse;
import com.ly.ecommerce.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .total(order.getTotal())
                .status(order.getStatus())
                .items(order.getItems().stream().map(i-> new OrderItemResponse(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getSubtotal()
                )).toList())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
