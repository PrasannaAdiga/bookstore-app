package com.learning.bookstore.application.port.in.cart;

import com.learning.bookstore.domain.Cart;

public interface ICartQueryService {
    Cart getCartByCartId(String cartId);
    Cart getCartByUserEmail();
}
