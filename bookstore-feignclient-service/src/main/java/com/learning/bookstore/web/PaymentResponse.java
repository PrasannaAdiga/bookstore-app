package com.learning.bookstore.web;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {
    private String paymentId;
    private LocalDateTime paymentDate;
    private boolean captured;
    private String receipt_url;
}
