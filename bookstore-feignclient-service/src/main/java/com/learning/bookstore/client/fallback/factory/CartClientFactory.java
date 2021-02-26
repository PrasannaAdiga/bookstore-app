package com.learning.bookstore.client.fallback.factory;


import com.learning.bookstore.client.fallback.handler.CartClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CartClientFactory implements FallbackFactory<CartClientHandler> {
    @Override
    public CartClientHandler create(Throwable cause) {
        log.error("Feign Client exception while calling Cart Services. Exception: ", cause.getMessage());
        return new CartClientHandler();
    }

}
