package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.CartItem;

import java.util.Optional;

public interface ICartItemDataManager {
    CartItem createCartItem(CartItem cartItem);
    CartItem removeCartItem(String cartItemId);
    CartItem removeAllCartItem(String cartId);
    Optional<CartItem> getCartItem(String cartItemId);
}
