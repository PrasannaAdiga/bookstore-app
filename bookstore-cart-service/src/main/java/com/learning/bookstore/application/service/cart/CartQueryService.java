package com.learning.bookstore.application.service.cart;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.cart.ICartQueryService;
import com.learning.bookstore.application.port.out.ICartDataProvider;
import com.learning.bookstore.domain.Cart;
import com.learning.bookstore.domain.CartItem;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CartQueryService implements ICartQueryService {
    private final ICartDataProvider cartDataManager;
    private final PrincipalResolver principalResolver;

    @Override
    public Cart getCartByCartId(String cartId) {
        Optional<Cart> cart = cartDataManager.getCartByCartId(cartId);
        cart.orElseThrow(() -> {
            log.error("ResourceNotFoundException in CartQueryService.getCartByCartId: Cart with Id {} not found", cartId);
            return new ResourceNotFoundException("Cart with Id " + cartId + " not found!");
        });
        return buildCart(cart.get());
    }

    @Override
    public Cart getCartByUserEmail() {
        String loggedInUserMail = principalResolver.getCurrentLoggedInUserMail();
        Optional<Cart> cart = cartDataManager.getCartByUserEmail(loggedInUserMail);
        cart.orElseThrow(() -> {
            log.error("ResourceNotFoundException in CartQueryService.getCartByUserEmail: No carts found for user with email Id {}", loggedInUserMail);
            return new ResourceNotFoundException("No carts found for user with email Id " + loggedInUserMail);
        });
        return buildCart(cart.get());
    }

    private Cart buildCart(Cart cart) {
        return Cart.builder()
                .id(cart.getId())
                .userEmail(cart.getUserEmail())
                .cartItems(cart.getCartItems())
                .totalPrice(getCartTotalPrice(cart))
                .build();
    }

    private double getCartTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(CartItem::getExtendedPrice)
                .sum();
    }

}
