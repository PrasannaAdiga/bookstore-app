package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.CartItem;

import java.util.Optional;

public interface ICartItemDataProvider {
    String createOrUpdateCartItem(CartItem cartItem);
    void removeCartItem(String cartItemId);
    void removeAllCartItem(String cartId);
    Optional<CartItem> getCartItem(String cartItemId);
}
