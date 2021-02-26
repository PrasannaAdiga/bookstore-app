package com.learning.bookstore.adapter.persistence.repository;

import com.learning.bookstore.adapter.persistence.entity.ShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ShippingAddressRepository extends JpaRepository<ShippingAddressEntity, String> {
    Optional<List<ShippingAddressEntity>> findByUserEmail(String userEmail);
}
