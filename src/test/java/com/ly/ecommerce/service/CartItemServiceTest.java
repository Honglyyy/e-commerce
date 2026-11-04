package com.ly.ecommerce.service;

import com.ly.ecommerce.dto.cartItemDto.CartItemRequest;
import com.ly.ecommerce.dto.cartItemDto.CartResponse;
import com.ly.ecommerce.model.CartItem;
import com.ly.ecommerce.model.Product;
import com.ly.ecommerce.model.User;
import com.ly.ecommerce.repository.CartItemRepository;
import com.ly.ecommerce.repository.ProductRepository;
import com.ly.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartItemService cartItemService;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testProduct = new Product();
        testProduct.setId(10L);
        testProduct.setName("Laptop");
        testProduct.setPrice(BigDecimal.valueOf(100.00));
        testProduct.setQuantity(5);
        testProduct.setActive(true);
    }

    @Test
    void addToCart_newProduct_success() {
        CartItemRequest request = new CartItemRequest(10L, 2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(10L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByUserAndProduct(testUser, testProduct)).thenReturn(Optional.empty());

        CartItem savedCartItem = new CartItem(1L, testUser, testProduct, 2, BigDecimal.valueOf(200.00), null, null);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedCartItem);
        when(cartItemRepository.findByUser(testUser)).thenReturn(List.of(savedCartItem));

        CartResponse response = cartItemService.addToCart("1", request);

        assertNotNull(response);
        assertEquals(1L, response.userId());
        assertEquals(1, response.items().size());
        assertEquals(10L, response.items().getFirst().productId());
        assertEquals(2, response.items().getFirst().quantity());
        assertEquals(BigDecimal.valueOf(200.00), response.items().getFirst().price());
    }

    @Test
    void addToCart_existingProduct_accumulatesQuantityAndPrice() {
        CartItemRequest request = new CartItemRequest(10L, 2);

        CartItem existingItem = new CartItem(1L, testUser, testProduct, 1, BigDecimal.valueOf(100.00), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(10L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByUserAndProduct(testUser, testProduct)).thenReturn(Optional.of(existingItem));

        CartItem updatedItem = new CartItem(1L, testUser, testProduct, 3, BigDecimal.valueOf(300.00), null, null);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(updatedItem);
        when(cartItemRepository.findByUser(testUser)).thenReturn(List.of(updatedItem));

        CartResponse response = cartItemService.addToCart("1", request);

        assertNotNull(response);
        assertEquals(1, response.items().size());
        assertEquals(3, response.items().getFirst().quantity());
        assertEquals(BigDecimal.valueOf(300.00), response.items().getFirst().price());
    }

    @Test
    void addToCart_exceedsStock_throwsException() {
        CartItemRequest request = new CartItemRequest(10L, 3);
        CartItem existingItem = new CartItem(1L, testUser, testProduct, 4, BigDecimal.valueOf(400.00), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(10L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.findByUserAndProduct(testUser, testProduct)).thenReturn(Optional.of(existingItem));

        // 4 existing + 3 requested = 7 > 5 stock
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cartItemService.addToCart("1", request));

        assertTrue(ex.getMessage().contains("Insufficient stock"));
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void addToCart_inactiveProduct_throwsException() {
        testProduct.setActive(false);
        CartItemRequest request = new CartItemRequest(10L, 1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(10L)).thenReturn(Optional.of(testProduct));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cartItemService.addToCart("1", request));

        assertTrue(ex.getMessage().contains("not active"));
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void addToCart_invalidQuantity_throwsException() {
        CartItemRequest request = new CartItemRequest(10L, 0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cartItemService.addToCart("1", request));

        assertTrue(ex.getMessage().contains("Quantity must be greater than zero"));
    }
}
