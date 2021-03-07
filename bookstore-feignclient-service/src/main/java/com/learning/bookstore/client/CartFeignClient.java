package com.learning.bookstore.client;

import com.learning.bookstore.web.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${cart.service.name:bookstore-cart-service}")
public interface CartFeignClient {
    @GetMapping("/v1/carts")
    CartResponse getCart(@RequestHeader(value = "Authorization", required = true) String accessToken);

    @DeleteMapping("/v1/carts/items")
    void removeAllCartItem(@RequestHeader(value = "Authorization", required = true) String accessToken, @RequestParam(value = "cartId", required = true) String cartId);

}
