package com.bookstore.learning.adapter.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("bookstore-order-service")
public interface OrderFeignClient {

}
