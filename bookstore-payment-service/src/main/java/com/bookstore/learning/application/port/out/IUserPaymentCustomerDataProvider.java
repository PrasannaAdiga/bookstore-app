package com.bookstore.learning.application.port.out;

import com.bookstore.learning.domain.UserPaymentCustomer;

import java.util.Optional;

public interface IUserPaymentCustomerDataProvider {
    String createUserPaymentCustomer(UserPaymentCustomer userPaymentCustomer);
    Optional<UserPaymentCustomer> getUserPaymentCustomerByUserEmail(String userEmail);

}
