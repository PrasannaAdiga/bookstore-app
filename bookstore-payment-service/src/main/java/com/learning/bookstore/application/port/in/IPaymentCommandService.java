package com.learning.bookstore.application.port.in;

import com.learning.bookstore.domain.Payment;
import com.learning.bookstore.domain.ProcessedPayment;

public interface IPaymentCommandService {
    ProcessedPayment createPayment(Payment payment);
}
