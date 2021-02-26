package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.ShippingAddress;

import java.util.List;
import java.util.Optional;

public interface IShippingAddressDataProvider {
    String createShippingAddress(ShippingAddress shippingAddress);
    void updateShippingAddress(ShippingAddress shippingAddress);
    void deleteShippingAddress(String shippingAddressId);
    Optional<List<ShippingAddress>> getAllShippingAddressOfUser(String userEmail);
    Optional<ShippingAddress> getShippingAddressById(String shippingAddressId);
}
