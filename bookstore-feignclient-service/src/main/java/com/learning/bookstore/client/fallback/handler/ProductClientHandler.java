package com.learning.bookstore.client.fallback.handler;


import com.learning.bookstore.client.ProductFeignClient;
import com.learning.bookstore.web.ProductResponse;

public class ProductClientHandler implements ProductFeignClient {
    @Override
    public ProductResponse getProductById(String id) {
        return ProductResponse.builder()
                .build();
    }

}
