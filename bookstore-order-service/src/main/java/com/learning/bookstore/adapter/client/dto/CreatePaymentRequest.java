package com.learning.bookstore.adapter.client.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CreatePaymentRequest {
    private int amount;
    private String currency;
    private String paymentMethodId;
}
