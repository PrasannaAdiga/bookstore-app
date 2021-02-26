package com.learning.bookstore.client.fallback.handler;


import com.learning.bookstore.client.PaymentFeignClient;
import com.learning.bookstore.web.CreatePaymentRequest;
import com.learning.bookstore.web.PaymentMethodResponse;
import com.learning.bookstore.web.PaymentResponse;

public class PaymentClientHandler implements PaymentFeignClient {

    @Override
    public PaymentMethodResponse getPaymentMethodOfUserById(String paymentMethodId) {
        return PaymentMethodResponse.builder().build();
    }

    @Override
    public PaymentResponse doPayment(CreatePaymentRequest createPaymentRequest) {
        return PaymentResponse.builder().build();
    }
}
