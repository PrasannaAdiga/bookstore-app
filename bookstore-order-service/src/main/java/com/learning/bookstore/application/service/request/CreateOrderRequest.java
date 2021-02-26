package com.learning.bookstore.application.service.request;


import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateOrderRequest {
    private String billingAddressId;

    @NotBlank(message = "Billing address Id should not be blank")
    private String shippingAddressId;

    @NotBlank(message = "Payment method Id should not be blank")
    private String paymentMethodId;
}
