package com.learning.bookstore.adapter.client;

import com.learning.bookstore.adapter.client.dto.BillingAddressResponse;
import com.learning.bookstore.adapter.client.dto.ShippingAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("bookstore-address-service")
public interface AddressFeignClient {

    @GetMapping("/v1/billing-addresses/{id}")
    BillingAddressResponse getBillingAddressById(@PathVariable("id") String id);

    @GetMapping("/v1/shipping-addresses/{id}")
    ShippingAddressResponse getShippingAddressById(@PathVariable("id") String id);

}
