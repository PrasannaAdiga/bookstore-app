package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.Cart;

import java.util.Optional;

public interface ICartDataManager {
    String createCart(Cart cart);
    Optional<Cart> getCartByCartId(String cartId);
    Optional<Cart> getCartByUserEmail(String userEmail);
}
