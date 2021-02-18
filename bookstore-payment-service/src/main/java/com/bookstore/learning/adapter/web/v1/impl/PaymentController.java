package com.bookstore.learning.adapter.web.v1.impl;

import com.bookstore.learning.adapter.web.v1.IPaymentController;
import com.bookstore.learning.adapter.web.v1.request.CreatePaymentRequest;
import com.bookstore.learning.adapter.web.v1.response.PaymentResponse;
import com.bookstore.learning.application.port.in.IPaymentCommandService;
import com.bookstore.learning.domain.Payment;
import com.bookstore.learning.domain.ProcessedPayment;
import com.bookstore.learning.infrastructure.annotation.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@WebAdapter
public class PaymentController implements IPaymentController {
    private final IPaymentCommandService paymentCommandService;

    @Override
    public ResponseEntity<PaymentResponse> doPayment(CreatePaymentRequest createPaymentRequest) {
        Payment payment = Payment.builder()
                .paymentMethodId(createPaymentRequest.getPaymentMethodId())
                .amount(createPaymentRequest.getAmount())
                .currency(createPaymentRequest.getCurrency())
                .build();
        ProcessedPayment processedPayment = paymentCommandService.createPayment(payment);
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(processedPayment.getPaymentId())
                .paymentDate(processedPayment.getPaymentDate())
                .captured(processedPayment.isCaptured())
                .receipt_url(processedPayment.getReceiptUrl())
                .build();
        return ResponseEntity.ok().body(paymentResponse);
    }
}
