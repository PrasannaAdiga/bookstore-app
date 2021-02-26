package com.learning.bookstore.web;

import lombok.Builder;

@Builder
public class PaymentMethodResponse {
    private String paymentMethodId;
    private String cardType;
    private String cardLast4Digits;
    private Long cardExpirationMonth;
    private Long cardExpirationYear;
    private String cardCountry;
}
