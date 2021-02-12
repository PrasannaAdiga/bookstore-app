package com.bookstore.learning.application.port.out;

import com.bookstore.learning.domain.BillingAddress;

import java.util.List;
import java.util.Optional;

public interface IBillingAddressDataProvider {
    String createBillingAddress(BillingAddress billingAddress);
    void updateBillingAddress(BillingAddress billingAddress);
    void deleteBillingAddress(String billingAddressId);
    Optional<List<BillingAddress>> getAllBillingAddressOfUser(String userEmail);
    Optional<BillingAddress> getBillingAddressById(String billingAddressId);
}
