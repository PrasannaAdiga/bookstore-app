package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.CartEntity;
import com.learning.bookstore.adapter.persistence.entity.CartItemEntity;
import com.learning.bookstore.adapter.persistence.repository.CartItemRepository;
import com.learning.bookstore.application.port.out.ICartItemDataProvider;
import com.learning.bookstore.domain.Cart;
import com.learning.bookstore.domain.CartItem;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class CartItemPersistenceAdapter implements ICartItemDataProvider {
    private final CartItemRepository cartItemRepository;

    @Override
    public String createOrUpdateCartItem(CartItem cartItem) {
        Optional<CartItemEntity> oCartItemEntity = cartItem.getId() != null ? cartItemRepository.findById(cartItem.getId()) : Optional.empty();
        CartItemEntity cartItemEntity = buildCartItemEntity(cartItem);
        if (oCartItemEntity.isPresent()) {
            cartItemEntity.setCreatedBy(oCartItemEntity.get().getCreatedBy());
            cartItemEntity.setCreationDate(oCartItemEntity.get().getCreationDate());
        }
        return cartItemRepository.save(cartItemEntity).getId();
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
                .id(cartItem.getId())
                .price(cartItem.getPrice())
                .extendedPrice(cartItem.getExtendedPrice())
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProductId())
                .cart(buildCartEntity(cartItem.getCart()))
                .build();
    }

    private CartItem buildCartItem(CartItemEntity cartItemEntity) {
        return CartItem.builder()
                .id(cartItemEntity.getId())
                .productId(cartItemEntity.getProductId())
                .price(cartItemEntity.getPrice())
                .extendedPrice(cartItemEntity.getExtendedPrice())
                .quantity(cartItemEntity.getQuantity())
                .cart(buildCart(cartItemEntity.getCart()))
                .build();
    }

    private CartEntity buildCartEntity(Cart cart) {
        return CartEntity.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .userEmail(cart.getUserEmail())
                .build();
    }

    private Cart buildCart(CartEntity cartEntity) {
        return Cart.builder()
                .id(cartEntity.getId())
                .totalPrice(cartEntity.getTotalPrice())
                .userEmail(cartEntity.getUserEmail())
                .build();
    }

}
