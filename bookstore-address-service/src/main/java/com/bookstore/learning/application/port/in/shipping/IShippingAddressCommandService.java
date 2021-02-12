package com.bookstore.learning.application.port.in.shipping;

import com.bookstore.learning.domain.ShippingAddress;

public interface IShippingAddressCommandService {
    String createShippingAddress(ShippingAddress shippingAddress);
    void updateShippingAddress(ShippingAddress shippingAddress);
    void deleteShippingAddress(String shippingAddressId);
}
