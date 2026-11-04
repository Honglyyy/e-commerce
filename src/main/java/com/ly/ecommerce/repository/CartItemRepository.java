package com.ly.ecommerce.repository;

import com.ly.ecommerce.model.CartItem;
import com.ly.ecommerce.model.Product;
import com.ly.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    List<CartItem> findByUser(User user);

    void deleteByUserAndProduct(User user, Product product);
}
