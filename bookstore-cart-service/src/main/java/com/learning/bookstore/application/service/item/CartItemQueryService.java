package com.learning.bookstore.application.service.item;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.item.ICartItemQueryService;
import com.learning.bookstore.application.port.out.ICartItemDataManager;
import com.learning.bookstore.domain.CartItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CartItemQueryService implements ICartItemQueryService {
    private final ICartItemDataManager cartItemDataManager;

    @Override
    public CartItem getCartItem(String cartItemId) {
        Optional<CartItem> cartItem = cartItemDataManager.getCartItem(cartItemId);
        return cartItem.orElseThrow(()-> {
            log.error("ResourceNotFoundException in CartItemQueryService.getCartItem: No Cart Item found with Id {}", cartItemId);
            throw new ResourceNotFoundException("No Cart Item found with Id " + cartItemId);
        });
    }
}
