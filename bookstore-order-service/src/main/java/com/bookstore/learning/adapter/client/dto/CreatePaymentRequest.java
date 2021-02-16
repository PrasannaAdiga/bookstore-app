package com.bookstore.learning.adapter.client.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CreatePaymentRequest {
    private int amount;
    private String currency;
    private String paymentMethodId;
}
