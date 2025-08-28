package com.Shawn.dream_shops.service.cart;

import com.Shawn.dream_shops.model.Cart;
import com.Shawn.dream_shops.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initialNewCart(User user);

    Cart getCartByUserId(Long userId);
}
