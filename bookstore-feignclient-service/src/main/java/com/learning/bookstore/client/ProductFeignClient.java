package com.learning.bookstore.client;

import com.learning.bookstore.web.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${product.service.name:bookstore-product-service}")
public interface ProductFeignClient {
    @GetMapping("/v1/products/{id}")
    ProductResponse getProductById(@RequestHeader(value = "Authorization", required = true) String accessToken, @PathVariable("id") String id);

}
