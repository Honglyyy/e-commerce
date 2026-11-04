package com.ly.ecommerce.service;

import com.ly.ecommerce.dto.cartItemDto.CartItemResponse;
import com.ly.ecommerce.dto.cartItemDto.CartResponse;
import com.ly.ecommerce.dto.orderDto.OrderRequest;
import com.ly.ecommerce.dto.orderDto.OrderResponse;
import com.ly.ecommerce.mapper.OrderMapper;
import com.ly.ecommerce.model.CartItem;
import com.ly.ecommerce.model.Order;
import com.ly.ecommerce.model.OrderItem;
import com.ly.ecommerce.model.User;
import com.ly.ecommerce.model.enums.OrderStatus;
import com.ly.ecommerce.repository.CartItemRepository;
import com.ly.ecommerce.repository.OrderRepository;
import com.ly.ecommerce.repository.ProductRepository;
import com.ly.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(String userId) {
        Long parsedUserId;
        try {
            parsedUserId = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        User user  = userRepository.findById(parsedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }



        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new  Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotal(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(i-> new OrderItem(
                        null,
                        i.getProduct(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getSubtotal(),
                        order
                )).toList();

        order.setItems(orderItems);

        orderRepository.save(order);

        cartService.clearCart(userId);

        return orderMapper.toOrderResponse(order);
    }
}
