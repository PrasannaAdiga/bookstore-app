package com.learning.bookstore.adapter.web.v1.impl;

import com.learning.bookstore.adapter.web.v1.ICartItemController;
import com.learning.bookstore.adapter.web.v1.request.CreateCartItemRequest;
import com.learning.bookstore.application.port.in.item.ICartItemCommandService;
import com.learning.bookstore.domain.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CartItemController implements ICartItemController {
    private final ICartItemCommandService cartItemCommandService;
    private static final String ACCESS_TOKEN_EMAIL_FIELD = "email";

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createCartItem(@Valid CreateCartItemRequest createCartItemRequest, @AuthenticationPrincipal Jwt principal) {
        CartItem cartItem = CartItem.builder()
                .productId(createCartItemRequest.getProductId())
                .quantity(createCartItemRequest.getQuantity())
                .build();
        String cartItemId = cartItemCommandService.createCartItem(principal.getClaimAsString(ACCESS_TOKEN_EMAIL_FIELD), cartItem);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cartItemId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<?> removeCartItem(@NotBlank(message = "Cart item ID should not be blank") String cartItemId) {
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
