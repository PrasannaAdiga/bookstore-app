package com.bookstore.learning.application.service;

import com.bookstore.learning.application.exception.ResourceNotFoundException;
import com.bookstore.learning.application.exception.StripeAPICallException;
import com.bookstore.learning.application.port.in.IPaymentCommandService;
import com.bookstore.learning.application.port.out.IUserPaymentCustomerDataProvider;
import com.bookstore.learning.domain.Payment;
import com.bookstore.learning.domain.ProcessedPayment;
import com.bookstore.learning.domain.UserPaymentCustomer;
import com.bookstore.learning.infrastructure.constant.PaymentServiceConstant;
import com.bookstore.learning.infrastructure.util.PrincipalResolver;
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
    private final PrincipalResolver principalResolver;

    @Override
    public ProcessedPayment createPayment(Payment payment) {
        String userEmail = principalResolver.getCurrentLoggedInUserMail();
        Optional<UserPaymentCustomer> userPaymentCustomer = userPaymentCustomerDataProvider.getUserPaymentCustomerByUserEmail(userEmail);
        exitIfNoPaymentCustomerFound(userPaymentCustomer);
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(buildCreatePaymentRequestParams(payment, userPaymentCustomer.get().getPaymentCustomerId()));
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
            throw new StripeAPICallException("Error while doing payment by user email " + userEmail + ". Exception: " + ex);
        }
    }

    private void exitIfNoPaymentCustomerFound(Optional<UserPaymentCustomer> userPaymentCustomer) {
        userPaymentCustomer.orElseThrow(() -> {
            log.error("ResourceNotFoundException in PaymentCommandService.createPayment: No User Payment Customer found for user email {}",
                    principalResolver.getCurrentLoggedInUserMail());
            throw new ResourceNotFoundException("No User Payment Customer found for user email " + principalResolver.getCurrentLoggedInUserMail());
        });
    }

    private Map<String, Object> buildCreatePaymentRequestParams(Payment payment, String paymentCustomer) {
        Map<String, Object> params = new HashMap<>();
        params.put(PaymentServiceConstant.PAYMENT_AMOUNT, payment.getAmount());
        params.put(PaymentServiceConstant.PAYMENT_CURRENCY, payment.getCurrency());
        params.put(PaymentServiceConstant.PAYMENT_METHOD, payment.getPaymentMethodId());
        params.put(PaymentServiceConstant.PAYMENT_CUSTOMER, paymentCustomer);
        params.put(PaymentServiceConstant.PAYMENT_CONFIRM, true);
        return  params;
    }
}
