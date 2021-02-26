package com.learning.bookstore.adapter.web.v1.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BillingAddressResponse {
    private String id;
    private String userEmail;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phone;
}
