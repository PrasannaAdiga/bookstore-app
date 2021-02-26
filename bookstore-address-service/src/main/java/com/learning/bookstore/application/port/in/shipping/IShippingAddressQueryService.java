package com.learning.bookstore.application.port.in.shipping;

import com.learning.bookstore.domain.ShippingAddress;

import java.util.List;

public interface IShippingAddressQueryService {
    List<ShippingAddress> getAllShippingAddressOfLoggedInUser();
    ShippingAddress getShippingAddressById(String shippingAddressId);
}
