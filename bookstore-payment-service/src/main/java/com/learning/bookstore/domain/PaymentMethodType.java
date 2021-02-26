package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class PaymentMethodType {
    @NotBlank(message = "Payment method Id should not be blank")
    private String id;

    @NotBlank(message = "Card type should not be blank")
    private String cardType;

    @NotBlank(message = "cardLast4Digits should not be blank")
    private String cardLast4Digits;

    @NotBlank(message = "Card expiration month should not be blank")
    private Long cardExpirationMonth;

    @NotBlank(message = "Card expiration year should not be blank")
    private Long cardExpirationYear;

    @NotBlank(message = "Card country should not be blank")
    private String cardCountry;
}
