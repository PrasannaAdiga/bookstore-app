package com.learning.bookstore.application.port.in.item;

import com.learning.bookstore.domain.CartItem;

public interface ICartItemCommandService {
    String createCartItem(String userEmail, CartItem cartItem);
    void removeCartItem(String cartItemId);
    void removeAllCartItem(String cartId);
}
