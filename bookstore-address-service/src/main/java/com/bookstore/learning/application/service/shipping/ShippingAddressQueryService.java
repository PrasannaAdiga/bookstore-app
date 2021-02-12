package com.bookstore.learning.application.service.shipping;

import com.bookstore.learning.application.exception.ResourceNotFoundException;
import com.bookstore.learning.application.port.in.shipping.IShippingAddressQueryService;
import com.bookstore.learning.application.port.out.IShippingAddressDataProvider;
import com.bookstore.learning.domain.ShippingAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ShippingAddressQueryService implements IShippingAddressQueryService {
    private final IShippingAddressDataProvider shippingAddressDataProvider;

    @Override
    public List<ShippingAddress> getAllShippingAddressOfUser(String userEmail) {
        Optional<List<ShippingAddress>> oShippingAddress = shippingAddressDataProvider.getAllShippingAddressOfUser(userEmail);
        oShippingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ShippingAddressQueryService.getAllShippingAddressOfUser: No shipping address found for the user {}", userEmail);
            throw new ResourceNotFoundException("No shipping address found for the user " + userEmail);
        });
        return oShippingAddress.get();
    }

    @Override
    public ShippingAddress getShippingAddressById(String shippingAddressId) {
        Optional<ShippingAddress> oShippingAddress = shippingAddressDataProvider.getShippingAddressById(shippingAddressId);
        oShippingAddress.orElseThrow(() -> {
            log.error("ResourceNotFoundException in ShippingAddressQueryService.getShippingAddressById: Shipping Address with id {} not found", shippingAddressId);
            throw new ResourceNotFoundException("Shipping Address with id " + shippingAddressId + " not found!");
        });
        return oShippingAddress.get();
    }
}
