package com.Shawn.dream_shops.repository;

import com.Shawn.dream_shops.model.Cart;
import com.Shawn.dream_shops.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
