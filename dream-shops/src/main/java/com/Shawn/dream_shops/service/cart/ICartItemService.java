package com.Shawn.dream_shops.service.cart;

import com.Shawn.dream_shops.model.CartItem;

public interface ICartItemService {
    void addCartItem(Long cartID, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    //這段完全是為了省上面的字用的
    CartItem getCartItem(Long cartId, Long productId);
}
