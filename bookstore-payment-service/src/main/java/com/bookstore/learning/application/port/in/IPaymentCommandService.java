package com.bookstore.learning.application.port.in;

import com.bookstore.learning.domain.Payment;
import com.bookstore.learning.domain.ProcessedPayment;

public interface IPaymentCommandService {
    ProcessedPayment createPayment(Payment payment);
}
