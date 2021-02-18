package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.ICartController;
import com.learning.bookstore.adapter.web.v1.response.CartItemResponse;
import com.learning.bookstore.adapter.web.v1.response.CartResponse;
import com.learning.bookstore.application.port.in.cart.ICartCommandService;
import com.learning.bookstore.application.port.in.cart.ICartQueryService;
import com.learning.bookstore.domain.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController implements ICartController {
    private final ICartCommandService cartCommandService;
    private final ICartQueryService cartQueryService;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createCart() {
        String cartId = cartCommandService.createCart();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cartId).toUri();
        return ResponseEntity.created(location).build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<CartResponse> getCart() {
        Cart cart = cartQueryService.getCartByUserEmail();
        List<CartItemResponse> cartItemResponses = buildCartItemResponse(cart);
        return ResponseEntity.ok().body(buildCartResponse(cart, cartItemResponses));
    }

    private List<CartItemResponse> buildCartItemResponse(Cart cart) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        cart.getCartItems().forEach(cartItem -> {
            cartItemResponses.add(CartItemResponse.builder()
                    .id(cartItem.getId())
                    .price(cartItem.getPrice())
                    .extendedPrice(cartItem.getExtendedPrice())
                    .productId(cartItem.getProductId())
                    .quantity(cartItem.getQuantity())
                    .build());
        });
        return cartItemResponses;
    }

    private CartResponse buildCartResponse(Cart cart, List<CartItemResponse> cartItemResponses) {
        return CartResponse.builder()
                .id(cart.getId())
                .userEmail(cart.getUserEmail())
                .totalPrice(cart.getTotalPrice())
                .cartItems(cartItemResponses)
                .build();
    }

}
