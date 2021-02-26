package com.learning.bookstore.client;

import com.learning.bookstore.client.fallback.factory.PaymentClientFactory;
import com.learning.bookstore.web.CreatePaymentRequest;
import com.learning.bookstore.web.PaymentMethodResponse;
import com.learning.bookstore.web.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${payment.service.name:bookstore-payment-service}",
        fallbackFactory = PaymentClientFactory.class)
public interface PaymentFeignClient {
    @GetMapping("/v1/payment-methods/{id}")
    PaymentMethodResponse getPaymentMethodOfUserById(@PathVariable(value = "id") String paymentMethodId);

    @PostMapping("/v1/payments")
    PaymentResponse doPayment(@RequestBody CreatePaymentRequest createPaymentRequest);

}
