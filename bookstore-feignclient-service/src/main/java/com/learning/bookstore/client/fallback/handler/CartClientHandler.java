package com.learning.bookstore.client.fallback.handler;


import com.learning.bookstore.client.CartFeignClient;
import com.learning.bookstore.web.CartResponse;
public class CartClientHandler implements CartFeignClient {
    @Override
    public CartResponse getCart() {
        return CartResponse.builder().build();
    }

    @Override
    public void removeAllCartItem(String cartId) {
    }

}
