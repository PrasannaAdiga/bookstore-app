package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.CartEntity;
import com.learning.bookstore.adapter.persistence.entity.CartItemEntity;
import com.learning.bookstore.adapter.persistence.repository.CartRepository;
import com.learning.bookstore.application.port.out.ICartDataProvider;
import com.learning.bookstore.domain.Cart;
import com.learning.bookstore.domain.CartItem;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class CartPersistenceAdapter implements ICartDataProvider {
    private final CartRepository cartRepository;

    @Override
    public String createCart(Cart cart) {
        return cartRepository.save(buildCartEntity(cart)).getId();
    }

    @Override
    public Optional<Cart> getCartByCartId(String cartId) {
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
        if (!cartEntity.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(buildCart(cartEntity.get()));
    }

    @Override
    public Optional<Cart> getCartByUserEmail(String userEmail) {
        Optional<CartEntity> cartEntity = cartRepository.findByUserEmail(userEmail);
        if (!cartEntity.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(buildCart(cartEntity.get()));
    }

    private CartEntity buildCartEntity(Cart cart) {
        return CartEntity.builder()
                .userEmail(cart.getUserEmail())
                .cartItems(new ArrayList<>())
                .build();
    }

    private Cart buildCart(CartEntity cartEntity) {
        List<CartItem> cartItems = cartEntity.getCartItems().stream().map(new Function<CartItemEntity, CartItem>() {
            @Override
            public CartItem apply(CartItemEntity cartItemEntity) {
                return buildCartItem(cartItemEntity);
            }
        }).collect(Collectors.toList());
        return Cart.builder()
                .id(cartEntity.getId())
                .userEmail(cartEntity.getUserEmail())
                .totalPrice(cartEntity.getTotalPrice())
                .cartItems(cartItems)
                .build();
    }

    private CartItem buildCartItem(CartItemEntity cartItemEntity) {
        return CartItem.builder()
                .id(cartItemEntity.getId())
                .price(cartItemEntity.getPrice())
                .extendedPrice(cartItemEntity.getExtendedPrice())
                .quantity(cartItemEntity.getQuantity())
                .productId(cartItemEntity.getProductId())
                .build();
    }

}
