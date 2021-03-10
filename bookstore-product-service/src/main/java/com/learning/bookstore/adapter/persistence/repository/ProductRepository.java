package com.learning.bookstore.adapter.persistence.repository;

import com.learning.bookstore.adapter.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findById(String id);
    Optional<ProductEntity> findByName(String name);
}
