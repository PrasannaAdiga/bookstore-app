package com.bookstore.learning.application.port.in;

import com.bookstore.learning.domain.PaymentMethodType;

import java.util.List;

public interface IPaymentMethodQueryService {
    List<PaymentMethodType> getAllPaymentMethodOfUser(String userEmail);
    PaymentMethodType getPaymentMethodOfUserById(String paymentMethodId, String userEmail);
}
