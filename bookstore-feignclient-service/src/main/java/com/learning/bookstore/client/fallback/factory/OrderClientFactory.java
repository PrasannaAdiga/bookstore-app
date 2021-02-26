package com.learning.bookstore.client.fallback.factory;


import com.learning.bookstore.client.fallback.handler.OrderClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderClientFactory implements FallbackFactory<OrderClientHandler> {
    @Override
    public OrderClientHandler create(Throwable cause) {
        log.error("Feign Client exception while calling Order Services. Exception: ", cause.getMessage());
        return new OrderClientHandler();
    }

}
