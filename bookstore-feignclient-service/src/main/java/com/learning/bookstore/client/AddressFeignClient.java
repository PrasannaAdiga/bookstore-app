package com.learning.bookstore.client;

import com.learning.bookstore.client.fallback.factory.AddressClientFactory;
import com.learning.bookstore.web.BillingAddressResponse;
import com.learning.bookstore.web.ShippingAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${address.service.name:bookstore-address-service}",
        fallbackFactory = AddressClientFactory.class)
public interface AddressFeignClient {
    @GetMapping("/v1/billing-addresses/{id}")
    BillingAddressResponse getBillingAddressById(@PathVariable("id") String id);

    @GetMapping("/v1/shipping-addresses/{id}")
    ShippingAddressResponse getShippingAddressById(@PathVariable("id") String id);

}
