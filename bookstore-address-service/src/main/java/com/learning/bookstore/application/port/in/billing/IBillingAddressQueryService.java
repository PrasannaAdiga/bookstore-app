package com.learning.bookstore.application.port.in.billing;

import com.learning.bookstore.domain.BillingAddress;

import java.util.List;

public interface IBillingAddressQueryService {
    List<BillingAddress> getAllBillingAddressOfLoggedInUser();
    BillingAddress getBillingAddressById(String billingAddressId);
}
