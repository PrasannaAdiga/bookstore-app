package com.bookstore.learning.adapter.persistence.repository;

import com.bookstore.learning.adapter.persistence.entity.ShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddressEntity, String> {
    Optional<List<ShippingAddressEntity>> findByUserEmail(String userEmail);
}
