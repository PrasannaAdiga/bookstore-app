package com.bookstore.learning.application.port.in.billing;

import com.bookstore.learning.domain.BillingAddress;

import java.util.List;

public interface IBillingAddressQueryService {
    List<BillingAddress> getAllBillingAddressOfUser(String userEmail);
    BillingAddress getBillingAddressById(String billingAddressId);
}
