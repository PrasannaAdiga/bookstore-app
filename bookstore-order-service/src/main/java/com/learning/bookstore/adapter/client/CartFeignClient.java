package com.learning.bookstore.adapter.client;

import com.learning.bookstore.adapter.client.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bookstore-cart-service")
public interface CartFeignClient {

    @GetMapping("/v1/carts")
    CartResponse getCart();

    @DeleteMapping("/v1/carts/items")
    void removeAllCartItem(@RequestParam(value = "cartId", required = true) String cartId);

}
