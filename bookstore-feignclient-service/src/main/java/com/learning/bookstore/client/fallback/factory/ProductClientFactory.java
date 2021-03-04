package com.learning.bookstore.client.fallback.factory;


import com.learning.bookstore.client.fallback.handler.ProductClientHandler;
import com.learning.bookstore.exception.FeignClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductClientFactory implements FallbackFactory<ProductClientHandler> {
    @Override
    public ProductClientHandler create(Throwable cause) {
        log.error("Feign Client exception while calling Product Service. Exception: {}", cause.toString());
        if(cause != null) {
            throw new FeignClientException(cause.toString());
        }
        return new ProductClientHandler();
    }

}
