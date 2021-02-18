package com.learning.bookstore.adapter.client;

import com.learning.bookstore.application.service.item.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("bookstore-product-service")
public interface ProductFeignClient {
    @GetMapping("/v1/products/{id}")
    ProductResponse getProductById(@PathVariable("id") String id);

}
