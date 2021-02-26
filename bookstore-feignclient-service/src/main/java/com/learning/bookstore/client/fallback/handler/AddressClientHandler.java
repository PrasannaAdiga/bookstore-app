package com.learning.bookstore.client.fallback.handler;


import com.learning.bookstore.client.AddressFeignClient;
import com.learning.bookstore.web.BillingAddressResponse;
import com.learning.bookstore.web.ShippingAddressResponse;

public class AddressClientHandler implements AddressFeignClient {
    @Override
    public BillingAddressResponse getBillingAddressById(String id) {
        return BillingAddressResponse.builder()
                .build();
    }

    @Override
    public ShippingAddressResponse getShippingAddressById(String id) {
        return ShippingAddressResponse.builder()
                .build();
    }

}
