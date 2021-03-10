package com.learning.bookstore.adapter.persistence.repository;

import com.learning.bookstore.adapter.persistence.entity.UserPaymentCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserPaymentCustomerRepository extends JpaRepository<UserPaymentCustomerEntity, String> {
    Optional<UserPaymentCustomerEntity> findByUserEmail(String userEmail);

}
