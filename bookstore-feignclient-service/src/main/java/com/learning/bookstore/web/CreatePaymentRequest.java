package com.learning.bookstore.web;

import lombok.Getter;


@Getter
public class CreatePaymentRequest {
    private int amount;
    private String currency;
    private String paymentMethodId;
}
