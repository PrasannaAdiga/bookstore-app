package com.learning.bookstore.application.port.in.billing;

import com.learning.bookstore.domain.BillingAddress;

public interface IBillingAddressCommandService {
    String createBillingAddress(BillingAddress billingAddress);
    void updateBillingAddress(BillingAddress billingAddress);
    void deleteBillingAddress(String billingAddressId);
}
