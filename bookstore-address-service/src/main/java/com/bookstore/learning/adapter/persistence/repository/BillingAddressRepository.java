package com.bookstore.learning.adapter.persistence.repository;

import com.bookstore.learning.adapter.persistence.entity.BillingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BillingAddressRepository extends JpaRepository<BillingAddressEntity, String> {
    Optional<List<BillingAddressEntity>> findByUserEmail(String userEmail);
}
