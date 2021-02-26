package com.learning.bookstore.adapter.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("bookstore-order-service")
public interface OrderFeignClient {

}
