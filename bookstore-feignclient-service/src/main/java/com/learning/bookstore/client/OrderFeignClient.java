package com.learning.bookstore.client;

import com.learning.bookstore.client.fallback.factory.OrderClientFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${order.service.name:bookstore-order-service}",
        fallbackFactory = OrderClientFactory.class)
public interface OrderFeignClient {

}
