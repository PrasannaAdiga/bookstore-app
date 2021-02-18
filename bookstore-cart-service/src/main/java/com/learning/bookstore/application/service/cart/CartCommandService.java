package com.learning.bookstore.application.service.cart;

import com.learning.bookstore.application.port.in.cart.ICartCommandService;
import com.learning.bookstore.application.port.out.ICartDataProvider;
import com.learning.bookstore.domain.Cart;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CartCommandService implements ICartCommandService {
    private final ICartDataProvider cartDataManager;
    private final PrincipalResolver principalResolver;

    @Override
    public String createCart() {
        String loggedInUserMail = principalResolver.getCurrentLoggedInUserMail();
        Optional<Cart> cart = cartDataManager.getCartByUserEmail(loggedInUserMail);
        if (cart.isPresent()) {
            return cart.get().getId();
        }
        return cartDataManager.createCart(Cart.builder()
                .cartItems(new ArrayList<>())
                .userEmail(loggedInUserMail)
                .build()
        );
    }
}
