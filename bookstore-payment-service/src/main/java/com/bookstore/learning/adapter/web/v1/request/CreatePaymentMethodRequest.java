package com.bookstore.learning.adapter.web.v1.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
public class CreatePaymentMethodRequest {
    @NotBlank(message = "First Name should not be blank")
    private String firstName;

    @NotBlank(message = "Last Name should not be blank")
    private String lastName;

    @NotBlank(message = "Card Number should not be blank")
    private String cardNumber;

    @NotBlank(message = "Last4digits should not be blank")
    private String last4Digits;

    @Positive(message = "Expiration Month should be positive")
    private int expirationMonth;

    @Positive(message = "Expiration Year should be positive")
    private int expirationYear;

    @Positive(message = "cvv should be positive")
    private int cvv;
}
