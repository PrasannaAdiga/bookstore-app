package com.learning.bookstore.client;

import com.learning.bookstore.client.fallback.factory.ProductClientFactory;
import com.learning.bookstore.web.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${product.service.name:bookstore-product-service}",
        fallbackFactory = ProductClientFactory.class)
public interface ProductFeignClient {
    @GetMapping("/v1/product/{id}")
    ProductResponse getProductById(@PathVariable("id") String id);

}
