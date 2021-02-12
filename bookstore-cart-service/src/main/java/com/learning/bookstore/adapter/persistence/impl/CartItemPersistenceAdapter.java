package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.CartItemEntity;
import com.learning.bookstore.adapter.persistence.repository.CartItemRepository;
import com.learning.bookstore.application.port.out.ICartItemDataProvider;
import com.learning.bookstore.domain.CartItem;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class CartItemPersistenceAdapter implements ICartItemDataProvider {
    private final CartItemRepository cartItemRepository;

    @Override
    public String createCartItem(CartItem cartItem) {
        return cartItemRepository.save(buildCartItemEntity(cartItem)).getId();
    }

    @Override
    public void removeCartItem(String cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void removeAllCartItem(String cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }

    @Override
    public Optional<CartItem> getCartItem(String cartItemId) {
        Optional<CartItemEntity> cartItemEntity = cartItemRepository.findById(cartItemId);
        if(!cartItemEntity.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(buildCartItem(cartItemEntity.get()));
    }

    private CartItemEntity buildCartItemEntity(CartItem cartItem) {
        return CartItemEntity.builder()
                .price(cartItem.getPrice())
                .extendedPrice(cartItem.getExtendedPrice())
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProductId())
                .cart(cartItem.getCart())
                .build();
    }

    private CartItem buildCartItem(CartItemEntity cartItemEntity) {
        return CartItem.builder()
                .id(cartItemEntity.getId())
                .productId(cartItemEntity.getProductId())
                .price(cartItemEntity.getPrice())
                .extendedPrice(cartItemEntity.getExtendedPrice())
                .quantity(cartItemEntity.getQuantity())
                .cart(cartItemEntity.getCart())
                .build();
    }
}
