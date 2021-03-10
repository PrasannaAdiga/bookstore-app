package com.learning.bookstore.adapter.persistence.repository;

import com.learning.bookstore.adapter.persistence.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, String> {
    Optional<ProductCategoryEntity> findById(String id);
    Optional<ProductCategoryEntity> findByName(String name);
}
