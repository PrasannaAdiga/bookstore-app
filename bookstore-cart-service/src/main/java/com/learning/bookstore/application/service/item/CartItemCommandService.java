package com.learning.bookstore.application.service.item;

import com.learning.bookstore.adapter.client.ProductFeignClient;
import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.port.in.cart.ICartQueryService;
import com.learning.bookstore.application.port.in.item.ICartItemCommandService;
import com.learning.bookstore.application.port.out.ICartItemDataProvider;
import com.learning.bookstore.application.service.item.response.ProductResponse;
import com.learning.bookstore.domain.Cart;
import com.learning.bookstore.domain.CartItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class CartItemCommandService implements ICartItemCommandService {
    private final ICartQueryService cartQueryService;
    private final ICartItemDataProvider cartItemDataManager;
    private final ProductFeignClient productFeignClient;

    @Override
    public String createCartItem(CartItem cartItem) {
        Cart cart = cartQueryService.getCartByUserEmail();
        ProductResponse productResponse = productFeignClient.getProductById(cartItem.getProductId());
//Comment the above line and un-comment below lines for standalone testing
//        ProductResponse productResponse = ProductResponse.builder()
//                .id(UUID.randomUUID().toString())
//                .availableCount(5)
//                .averageRating(3.5)
//                .categoryId("f23bfeed-23a8-45d8-a702-1614dacdd849")
//                .description("Description")
//                .imageId(UUID.randomUUID().toString())
//                .noOfRatings(10)
//                .name("Mac Book Air")
//                .price(70000)
//                .build();
        exitIfNoEnoughQuantityFound(productResponse.getAvailableCount(), cartItem.getQuantity());
        return createOrUpdateCartItem(cart, productResponse, cartItem.getQuantity());
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

    private void exitIfNoEnoughQuantityFound(int availableProductQuantity, int requestedProductQuantity) {
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
            return cartItemDataManager.createOrUpdateCartItem(cItem);
        }
        CartItem cartItem = CartItem.builder()
                .productId(productResponse.getId())
                .price(productResponse.getPrice())
                .extendedPrice(requestedQuantity * productResponse.getPrice())
                .quantity(requestedQuantity)
                .cart(cart)
                .build();
        return cartItemDataManager.createOrUpdateCartItem(cartItem);
    }

}
