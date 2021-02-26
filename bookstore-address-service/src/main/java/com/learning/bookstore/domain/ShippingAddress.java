package com.learning.bookstore.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class ShippingAddress {
    private String id;

    @NotBlank(message = "User Email should not be blank")
    private String userEmail;

    @NotBlank(message = "AddressLine1 should not be blank")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City should not be blank")
    private String city;

    @NotBlank(message = "State should not be blank")
    private String state;

    @NotBlank(message = "Postal Code should not be blank")
    private String postalCode;

    @NotBlank(message = "Country should not be blank")
    @Pattern(regexp = "[A-Z]{2}", message = "2-letter ISO country code required")
    private String country;

    @NotBlank(message = "Phone should not be blank")
    private String phone;

}
