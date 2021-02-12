package com.learning.bookstore.application.service.cart;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.cart.ICartQueryService;
import com.learning.bookstore.application.port.out.ICartDataProvider;
import com.learning.bookstore.domain.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CartQueryService implements ICartQueryService {
    private final ICartDataProvider cartDataManager;

    @Override
    public Cart getCartByCartId(String cartId) {
        Optional<Cart> cart = cartDataManager.getCartByCartId(cartId);
        return cart.orElseThrow(() -> {
            log.error("ResourceNotFoundException in CartQueryService.getCartByCartId: Cart with Id {} not found", cartId);
            return new ResourceNotFoundException("Cart with Id " + cartId + " not found!");
        });
    }

    @Override
    public Cart getCartByUserEmail(String userEmail) {
        Optional<Cart> cart = cartDataManager.getCartByUserEmail(userEmail);
        return cart.orElseThrow(() -> {
            log.error("ResourceNotFoundException in CartQueryService.getCartByUserEmail: No carts found for user with email Id {}", userEmail);
            return new ResourceNotFoundException("No carts found for user with email Id " + userEmail);
        });
    }

}
