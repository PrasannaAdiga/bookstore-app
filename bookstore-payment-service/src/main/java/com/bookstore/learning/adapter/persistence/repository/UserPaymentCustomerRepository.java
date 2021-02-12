package com.bookstore.learning.adapter.persistence.repository;

import com.bookstore.learning.adapter.persistence.entity.UserPaymentCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPaymentCustomerRepository extends JpaRepository<UserPaymentCustomerEntity, String> {
    Optional<UserPaymentCustomerEntity> findByUserEmail(String userEmail);

}
