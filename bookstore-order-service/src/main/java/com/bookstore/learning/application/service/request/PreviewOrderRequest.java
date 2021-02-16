package com.bookstore.learning.application.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreviewOrderRequest {
    private String billingAddressId;

    @NotBlank(message = "Billing address Id should not be blank")
    private String shippingAddressId;

    @NotBlank(message = "Payment method Id should not be blank")
    private String paymentMethodId;
}
