package com.bookstore.learning.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class UserPaymentCustomer {
    private String id;

    @NotBlank(message = "User Email should not be blank")
    private String userEmail;

    private String paymentCustomerId;
}
