package com.learning.bookstore.application.port.in;

import com.learning.bookstore.domain.PaymentMethodType;

import java.util.List;

public interface IPaymentMethodQueryService {
    List<PaymentMethodType> getAllPaymentMethodOfUser();
    PaymentMethodType getPaymentMethodOfUserById(String paymentMethodId);
}
