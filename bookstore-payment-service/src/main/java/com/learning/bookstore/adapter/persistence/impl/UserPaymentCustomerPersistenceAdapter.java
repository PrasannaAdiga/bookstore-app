package com.learning.bookstore.adapter.persistence.impl;

import com.learning.bookstore.adapter.persistence.entity.UserPaymentCustomerEntity;
import com.learning.bookstore.adapter.persistence.repository.UserPaymentCustomerRepository;
import com.learning.bookstore.application.port.out.IUserPaymentCustomerDataProvider;
import com.learning.bookstore.domain.UserPaymentCustomer;
import com.learning.bookstore.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPaymentCustomerPersistenceAdapter implements IUserPaymentCustomerDataProvider {
    private final UserPaymentCustomerRepository userPaymentCustomerRepository;

    @Override
    public String createUserPaymentCustomer(UserPaymentCustomer userPaymentCustomer) {
        UserPaymentCustomerEntity userPaymentCustomerEntity = UserPaymentCustomerEntity.builder()
                .userEmail(userPaymentCustomer.getUserEmail())
                .paymentCustomerId(userPaymentCustomer.getPaymentCustomerId())
                .build();
        return userPaymentCustomerRepository.save(userPaymentCustomerEntity).getId();
    }

    @Override
    public Optional<UserPaymentCustomer> getUserPaymentCustomerByUserEmail(String userEmail) {
        Optional<UserPaymentCustomerEntity> userPaymentCustomerEntity = userPaymentCustomerRepository.findByUserEmail(userEmail);
        if(!userPaymentCustomerEntity.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(UserPaymentCustomer.builder()
                .id(userPaymentCustomerEntity.get().getId())
                .userEmail(userPaymentCustomerEntity.get().getUserEmail())
                .paymentCustomerId(userPaymentCustomerEntity.get().getPaymentCustomerId())
                .build());
    }
}
