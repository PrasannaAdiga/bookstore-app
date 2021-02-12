package com.bookstore.learning.adapter.web.v1.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class PaymentResponse {
    private String paymentId;
    private LocalDateTime paymentDate;
    private boolean captured;
    private String receipt_url;
}
