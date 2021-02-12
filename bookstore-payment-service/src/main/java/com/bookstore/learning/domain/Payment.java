package com.bookstore.learning.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Builder
public class Payment {
    @Positive(message = "Amount should be positive")
    private int amount;

    @NotBlank(message = "Currency should not be blank")
    private String currency;

    @NotBlank(message = "Payment method Id should not be blank")
    private String paymentMethodId;
}
