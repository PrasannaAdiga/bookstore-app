package com.learning.bookstore.client.fallback.factory;


import com.learning.bookstore.client.fallback.handler.PaymentClientHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentClientFactory implements FallbackFactory<PaymentClientHandler> {
    @Override
    public PaymentClientHandler create(Throwable cause) {
        log.error("Feign Client exception while calling Payment Services. Exception: ", cause.getMessage());
        return new PaymentClientHandler();
    }

}
