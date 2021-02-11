package com.learning.bookstore.application.service.cart;

import com.learning.bookstore.application.port.in.cart.ICartCommandService;
import com.learning.bookstore.application.port.out.ICartDataManager;
import com.learning.bookstore.domain.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CartCommandService implements ICartCommandService {
    private final ICartDataManager cartDataManager;

    @Override
    public String createCart(String userEmailId) {
        Optional<Cart> cart = cartDataManager.getCartByUserEmail(userEmailId);
        if (cart.isPresent()) {
            return cart.get().getId();
        }
        return cartDataManager.createCart(
                Cart.builder()
                        .cartItems(new ArrayList<>())
                        .userEmail(userEmailId)
                        .build()
        );
    }
}
