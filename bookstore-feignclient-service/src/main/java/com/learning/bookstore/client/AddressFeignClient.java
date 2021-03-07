package com.learning.bookstore.client;

import com.learning.bookstore.web.BillingAddressResponse;
import com.learning.bookstore.web.ShippingAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${address.service.name:bookstore-address-service}")
public interface AddressFeignClient {
    @GetMapping("/v1/billing-addresses/{id}")
    BillingAddressResponse getBillingAddressById(@RequestHeader(value = "Authorization", required = true) String accessToken, @PathVariable("id") String id);

    @GetMapping("/v1/shipping-addresses/{id}")
    ShippingAddressResponse getShippingAddressById(@RequestHeader(value = "Authorization", required = true) String accessToken, @PathVariable("id") String id);

}
