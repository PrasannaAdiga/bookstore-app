package com.bookstore.learning.application.port.in.billing;

import com.bookstore.learning.domain.BillingAddress;

public interface IBillingAddressCommandService {
    String createBillingAddress(BillingAddress billingAddress);
    void updateBillingAddress(BillingAddress billingAddress);
    void deleteBillingAddress(String billingAddressId);
}
