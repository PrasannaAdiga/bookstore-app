package com.learning.bookstore.application.port.in.item;

import com.learning.bookstore.domain.CartItem;

public interface ICartItemQueryService {
    CartItem getCartItem(String cartItemId);
}
