package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.ICartController;
import com.learning.bookstore.adapter.web.v1.response.CartItemResponse;
import com.learning.bookstore.adapter.web.v1.response.CartResponse;
import com.learning.bookstore.application.port.in.cart.ICartCommandService;
import com.learning.bookstore.application.port.in.cart.ICartQueryService;
import com.learning.bookstore.domain.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private static final String ACCESS_TOKEN_EMAIL_FIELD = "email";

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createCart(@AuthenticationPrincipal Jwt principal) {
        String cartId = cartCommandService.createCart(principal.getClaimAsString(ACCESS_TOKEN_EMAIL_FIELD));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cartId).toUri();
        return ResponseEntity.created(location).build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal Jwt principal) {
        Cart cart = cartQueryService.getCartByUserEmail(principal.getClaimAsString(ACCESS_TOKEN_EMAIL_FIELD));
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
        CartResponse cartResponse = CartResponse.builder()
                .id(cart.getId())
                .userEmail(cart.getUserEmail())
                .totalPrice(cart.getTotalPrice())
                .cartItems(cartItemResponses)
                .build();
        return ResponseEntity.ok().body(cartResponse);
    }


}
