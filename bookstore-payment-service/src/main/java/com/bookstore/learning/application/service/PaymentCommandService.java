package com.bookstore.learning.application.service;

import com.bookstore.learning.application.exception.StripeAPICallException;
import com.bookstore.learning.application.port.in.IPaymentCommandService;
import com.bookstore.learning.application.port.out.IUserPaymentCustomerDataProvider;
import com.bookstore.learning.domain.Payment;
import com.bookstore.learning.domain.ProcessedPayment;
import com.bookstore.learning.domain.UserPaymentCustomer;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@RequiredArgsConstructor
@Slf4j
public class PaymentCommandService implements IPaymentCommandService {
    private final IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider;

    @Override
    public ProcessedPayment createPayment(Payment payment, String userEmail) {
        Optional<UserPaymentCustomer> userPaymentCustomer = userPaymentCustomerDataProvider.getUserPaymentCustomerByUserEmail(userEmail);
        Map<String, Object> params = new HashMap<>();
        params.put("amount", payment.getAmount());
        params.put("currency", payment.getCurrency());
        params.put("payment_method", payment.getPaymentMethodId());
        params.put("customer", userPaymentCustomer.get().getPaymentCustomerId());
        params.put("confirm", true);
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            Optional<Charge> paidRecord = paymentIntent.getCharges().getData().stream().filter(Charge::getPaid).findAny();
            if (paidRecord.isPresent()) {
                LocalDateTime paymentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(paidRecord.get().getCreated()), TimeZone.getDefault().toZoneId());
                return ProcessedPayment.builder()
                        .paymentId(paidRecord.get().getId())
                        .paymentDate(paymentTime)
                        .captured(true)
                        .receiptUrl(paidRecord.get().getReceiptUrl())
                        .build();
            } else {
                return ProcessedPayment.builder()
                        .captured(false)
                        .build();
            }
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentCommandService.createPayment: " +
                    "Error while doing payment by user email {}, Exception: {}", userEmail, ex);
            throw new StripeAPICallException("Error while doing payment by user email " + userEmail);
        }
    }
}
