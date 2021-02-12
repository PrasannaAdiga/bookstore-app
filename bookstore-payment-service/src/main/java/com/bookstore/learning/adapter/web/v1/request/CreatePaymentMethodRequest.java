package com.bookstore.learning.adapter.web.v1.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

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

    @NotBlank(message = "Expiration Month should not be blank")
    private int expirationMonth;

    @NotBlank(message = "Expiration Year should not be blank")
    private int expirationYear;

    @NotBlank(message = "cvv should not be blank")
    private int cvv;
}
