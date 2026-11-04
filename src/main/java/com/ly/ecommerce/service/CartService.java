package com.ly.ecommerce.service;

import com.ly.ecommerce.dto.cartItemDto.CartItemRequest;
import com.ly.ecommerce.dto.cartItemDto.CartItemResponse;
import com.ly.ecommerce.dto.cartItemDto.CartResponse;
import com.ly.ecommerce.model.CartItem;
import com.ly.ecommerce.model.Product;
import com.ly.ecommerce.model.User;
import com.ly.ecommerce.repository.CartItemRepository;
import com.ly.ecommerce.repository.ProductRepository;
import com.ly.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartResponse addToCart(String userId, CartItemRequest cartItemRequest) {
        if (cartItemRequest == null || cartItemRequest.productId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        if (cartItemRequest.quantity() == null || cartItemRequest.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        User user = getUserCart(userId);

        Product product = productRepository.findById(cartItemRequest.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalArgumentException("Product is not active");
        }

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUser(user);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        int newTotalQuantity = cartItem.getQuantity() + cartItemRequest.quantity();
        int availableStock = product.getQuantity() != null ? product.getQuantity() : 0;
        if (availableStock < newTotalQuantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + availableStock);
        }

        BigDecimal unitPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        cartItem.setQuantity(newTotalQuantity);
        cartItem.setPrice(unitPrice.multiply(BigDecimal.valueOf(newTotalQuantity)));
        cartItemRepository.save(cartItem);

        return getCartResponse(user.getId(), user);
    }

    @Transactional
    public CartResponse removeFromCart(String userId, Long productId) {

        User user = getUserCart(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));


        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        int newTotalQuantity = cartItem.getQuantity() - 1;

        BigDecimal unitPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        cartItem.setQuantity(newTotalQuantity);
        cartItem.setPrice(unitPrice.multiply(BigDecimal.valueOf(newTotalQuantity)));
        cartItemRepository.save(cartItem);

        return getCartResponse(user.getId(), user);
    }

    public CartResponse getCartItems(String userId) {
        Long parsedUserId;
        try {
            parsedUserId = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        User user  = userRepository.findById(parsedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return getCartResponse(parsedUserId, user);
    }

    // Get cart response utility method
    public User getUserCart(String userId) {
        Long parsedUserId;
        try {
            parsedUserId = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        User user  = userRepository.findById(parsedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user;
    }

    private CartResponse getCartResponse(Long userId, User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(item -> CartItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();


        return CartResponse.builder()
                .userId(userId)
                .items(itemResponses)
                .build();
    }
}
