package com.bookstore.learning.application.port.in.shipping;

import com.bookstore.learning.domain.ShippingAddress;

import java.util.List;

public interface IShippingAddressQueryService {
    List<ShippingAddress> getAllShippingAddressOfUser(String userEmail);
    ShippingAddress getShippingAddressById(String shippingAddressId);
}
