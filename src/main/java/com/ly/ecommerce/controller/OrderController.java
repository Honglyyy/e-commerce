package com.ly.ecommerce.controller;

import com.ly.ecommerce.dto.orderDto.OrderRequest;
import com.ly.ecommerce.dto.orderDto.OrderResponse;
import com.ly.ecommerce.repository.OrderRepository;
import com.ly.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-ID") String userId
    ){
        return new ResponseEntity(
                orderService.createOrder(userId),
                HttpStatus.CREATED
        );
    }
}
