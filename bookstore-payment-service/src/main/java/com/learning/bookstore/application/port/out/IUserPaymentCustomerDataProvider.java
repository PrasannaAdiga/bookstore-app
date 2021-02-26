package com.learning.bookstore.application.port.out;

import com.learning.bookstore.domain.UserPaymentCustomer;

import java.util.Optional;

public interface IUserPaymentCustomerDataProvider {
    String createUserPaymentCustomer(UserPaymentCustomer userPaymentCustomer);
    Optional<UserPaymentCustomer> getUserPaymentCustomerByUserEmail(String userEmail);

}
