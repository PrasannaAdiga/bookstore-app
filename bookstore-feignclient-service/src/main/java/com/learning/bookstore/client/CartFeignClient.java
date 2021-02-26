package com.learning.bookstore.client;

import com.learning.bookstore.client.fallback.factory.CartClientFactory;
import com.learning.bookstore.web.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${cart.service.name:bookstore-cart-service}",
        fallbackFactory = CartClientFactory.class)
public interface CartFeignClient {
    @GetMapping("/v1/carts")
    CartResponse getCart();

    @DeleteMapping("/v1/carts/items")
    void removeAllCartItem(@RequestParam(value = "cartId", required = true) String cartId);

}
