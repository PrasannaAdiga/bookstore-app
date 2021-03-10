package com.learning.bookstore.adapter.persistence.repository;

import com.learning.bookstore.adapter.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<CartEntity, String> {
    Optional<CartEntity> findByUserEmail(String userEmail);
}
