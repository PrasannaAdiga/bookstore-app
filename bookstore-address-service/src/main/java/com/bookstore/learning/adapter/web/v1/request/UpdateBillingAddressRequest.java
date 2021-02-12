package com.bookstore.learning.adapter.web.v1.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateBillingAddressRequest extends AddressRequest {
    @NotBlank(message = "Billing address Id should not be blank")
    private String id;

}
