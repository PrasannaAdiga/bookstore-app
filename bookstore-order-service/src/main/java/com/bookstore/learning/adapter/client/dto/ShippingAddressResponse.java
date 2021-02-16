package com.bookstore.learning.adapter.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShippingAddressResponse {
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
