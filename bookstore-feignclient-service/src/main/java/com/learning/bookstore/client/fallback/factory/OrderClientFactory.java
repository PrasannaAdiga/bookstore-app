package com.learning.bookstore.client.fallback.factory;


import com.learning.bookstore.client.OrderFeignClient;
import com.learning.bookstore.client.fallback.handler.OrderClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderClientFactory implements FallbackFactory<OrderFeignClient> {
    @Override
    public OrderFeignClient create(Throwable cause) {
        return new OrderClientHandler(cause);
    }

}
