package com.learning.bookstore.client;

import com.learning.bookstore.web.CreatePaymentRequest;
import com.learning.bookstore.web.PaymentMethodResponse;
import com.learning.bookstore.web.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${payment.service.name:bookstore-payment-service}")
public interface PaymentFeignClient {
    @GetMapping("/v1/payment-methods/{id}")
    PaymentMethodResponse getPaymentMethodOfUserById(@RequestHeader(value = "Authorization", required = true) String accessToken, @PathVariable(value = "id") String paymentMethodId);

    @PostMapping("/v1/payments")
    PaymentResponse doPayment(@RequestHeader(value = "Authorization", required = true) String accessToken, @RequestBody CreatePaymentRequest createPaymentRequest);

}
