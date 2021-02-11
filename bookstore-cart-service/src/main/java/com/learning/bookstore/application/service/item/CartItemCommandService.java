package com.learning.bookstore.application.service.item;

import com.learning.bookstore.adapter.client.ProductFeignClient;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.cart.ICartQueryService;
import com.learning.bookstore.application.port.in.item.ICartItemCommandService;
import com.learning.bookstore.application.port.out.ICartItemDataManager;
import com.learning.bookstore.application.service.item.response.ProductResponse;
import com.learning.bookstore.domain.Cart;
import com.learning.bookstore.domain.CartItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CartItemCommandService implements ICartItemCommandService {
    private final ICartQueryService cartQueryService;
    private final ICartItemDataManager cartItemDataManager;
    private final ProductFeignClient productFeignClient;

    @Override
    public void createCartItem(String userEmail, CartItem cartItem) {
        Cart cart = cartQueryService.getCartByUserEmail(userEmail);
        exitIfNoCartFound(userEmail, cart);
        ProductResponse productResponse = productFeignClient.getProductById(cartItem.getProductId());
        exitIfNoProductFound(productResponse, cartItem);
        exitIfEnoughQuantityNotFound(productResponse.getAvailableCount(), cartItem.getQuantity());
        createOrUpdateCartItem(cart, productResponse, cartItem.getQuantity());
    }

    @Override
    public void removeCartItem(String cartItemId) {
        Optional<CartItem> cartItem = cartItemDataManager.getCartItem(cartItemId);
        cartItem.orElseThrow(() -> {
            log.error("ResourceNotFoundException in CartItemCommandService.removeCartItem: No Cart Item found with Id {}", cartItemId);
            return new ResourceNotFoundException("No Cart Item found with Id " + cartItemId);
        });
        cartItemDataManager.removeCartItem(cartItemId);
    }

    @Override
    public void removeAllCartItem(String cartId) {
        Cart cart = cartQueryService.getCartByCartId(cartId);
        Optional.of(cart).orElseThrow(() -> {
            log.error("ResourceNotFoundException in CartItemCommandService.removeAllCartItem: No cart found with Id {}", cartId);
            return new ResourceNotFoundException("No carts found with Id " + cartId);
        });
        cartItemDataManager.removeAllCartItem(cartId);
    }

    private void exitIfNoCartFound(String userEmail, Cart cart) {
        Optional.of(cart).orElseThrow(() -> {
            log.error("ResourceNotFoundException in CartItemCommandService.createCartItem: No carts found for user with email Id {}", userEmail);
            return new ResourceNotFoundException("No carts found for user with email Id " + userEmail);
        });
    }

    private void exitIfNoProductFound(ProductResponse productResponse, CartItem cartItem) {
        if(productResponse == null) {
            log.error("ResourceNotFoundException in CartItemCommandService.createCartItem: No Product found for the CartItem Id {} with product Id {}", cartItem.getId(), cartItem.getProductId());
            throw new ResourceNotFoundException("No Product found for the CartItem Id " + cartItem.getId() + " with product Id " + cartItem.getProductId());
        }
    }

    private void exitIfEnoughQuantityNotFound(int availableProductQuantity, int requestedProductQuantity) {
        if (requestedProductQuantity > availableProductQuantity) {
            log.error("ResourceNotFoundException in CartItemCommandService.createCartItem: Not enough quantity found. Requested product item counts {}. " +
                    "Available product counts {}", requestedProductQuantity, availableProductQuantity);
            throw new ResourceNotFoundException("Not enough quantity found. Requested product item counts " + requestedProductQuantity +
                    "Available product counts " + availableProductQuantity);
        }
    }

    private String createOrUpdateCartItem(Cart cart, ProductResponse productResponse, int requestedQuantity) {
        Optional<CartItem> oCartItem = cart.getCartItems().stream().filter(cItem -> productResponse.getId().equalsIgnoreCase(cItem.getProductId())).findFirst();
        if(oCartItem.isPresent()) {
            CartItem cartItem = oCartItem.get();
            CartItem cItem = CartItem.builder()
                    .id(cartItem.getId())
                    .productId(cartItem.getProductId())
                    .price(productResponse.getPrice())
                    .extendedPrice(productResponse.getPrice() * cartItem.getQuantity())
                    .quantity(requestedQuantity)
                    .cart(cart)
                    .build();
            return cartItemDataManager.createCartItem(cItem).getId();
        }
        CartItem cartItem = CartItem.builder()
                .productId(productResponse.getId())
                .price(productResponse.getPrice())
                .extendedPrice(requestedQuantity * productResponse.getPrice())
                .quantity(requestedQuantity)
                .cart(cart)
                .build();
        return cartItemDataManager.createCartItem(cartItem).getId();
    }

}
