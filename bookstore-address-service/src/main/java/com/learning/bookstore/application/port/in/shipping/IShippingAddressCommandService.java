package com.learning.bookstore.application.port.in.shipping;

import com.learning.bookstore.domain.ShippingAddress;

public interface IShippingAddressCommandService {
    String createShippingAddress(ShippingAddress shippingAddress);
    void updateShippingAddress(ShippingAddress shippingAddress);
    void deleteShippingAddress(String shippingAddressId);
}
