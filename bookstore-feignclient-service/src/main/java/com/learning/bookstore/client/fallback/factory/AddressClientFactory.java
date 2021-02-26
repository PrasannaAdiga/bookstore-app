package com.learning.bookstore.client.fallback.factory;


import com.learning.bookstore.client.fallback.handler.AddressClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressClientFactory implements FallbackFactory<AddressClientHandler> {
    @Override
    public AddressClientHandler create(Throwable cause) {
        log.error("Feign Client exception while calling Address Services. Exception: ", cause.getMessage());
        return new AddressClientHandler();
    }

}
