package com.bookstore.learning.adapter.client;

import com.bookstore.learning.adapter.client.dto.CreatePaymentRequest;
import com.bookstore.learning.adapter.client.dto.PaymentMethodResponse;
import com.bookstore.learning.adapter.client.dto.CreatePaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("bookstore-payment-service")
public interface PaymentFeignClient {

    @GetMapping("/v1/payment-methods/{id}")
    PaymentMethodResponse getPaymentMethodOfUserById(@PathVariable(value = "id") String paymentMethodId);

    @PostMapping("/v1/payments")
    CreatePaymentResponse doPayment(@RequestBody CreatePaymentRequest createPaymentRequest);

}
