package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.ICartItemController;
import com.learning.bookstore.adapter.web.v1.request.CreateCartItemRequest;
import com.learning.bookstore.application.port.in.item.ICartItemCommandService;
import com.learning.bookstore.domain.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CartItemController implements ICartItemController {
    private final ICartItemCommandService cartItemCommandService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createCartItem(CreateCartItemRequest createCartItemRequest) {
        CartItem cartItem = CartItem.builder()
                .productId(createCartItemRequest.getProductId())
                .quantity(createCartItemRequest.getQuantity())
                .build();
        String cartItemId = cartItemCommandService.createCartItem(cartItem);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cartItemId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<?> removeCartItem(String cartItemId) {
        cartItemCommandService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> removeAllCartItem(String cartId) {
        cartItemCommandService.removeAllCartItem(cartId);
        return ResponseEntity.noContent().build();
    }

    /*============================================ Query Section ================================================*/

}
